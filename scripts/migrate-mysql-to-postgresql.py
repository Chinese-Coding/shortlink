#!/usr/bin/env python3
"""Copy ShortLink's physical MySQL tables into an initialized PostgreSQL database."""

import argparse
from contextlib import closing
from datetime import datetime, timezone
from hashlib import sha256
import os
from pathlib import Path
import re
import sys
from zoneinfo import ZoneInfo

import psycopg
from psycopg import sql
import pymysql

# MySQL datetime 不带时区，应用按 Asia/Shanghai 写入；若源库不是该时区，用 MYSQL_TIMEZONE 覆盖。
SOURCE_TIMEZONE = ZoneInfo(os.getenv("MYSQL_TIMEZONE", "Asia/Shanghai"))
SCHEMA = Path(__file__).resolve().parents[1] / "link-postgresql.sql"
SHARD_COUNT = 16


def schema_tables():
    # SQL 只显式创建 *_0 模板表，其余分表由 DO 块用 LIKE 复制，这里按分片数展开。
    tables = []
    for name in re.findall(r"^CREATE TABLE (\w+) ", SCHEMA.read_text(encoding="utf-8"), re.M):
        template = re.fullmatch(r"(.+)_0", name)
        tables += [f"{template[1]}_{shard}" for shard in range(SHARD_COUNT)] if template else [name]
    return tuple(sorted(tables))


TABLES = schema_tables()
STAT_COLUMNS = {"date": "stats_date", "hour": "stats_hour", "weekday": "stats_weekday"}


def mysql_identifier(name):
    return "`" + name.replace("`", "``") + "`"


def source_tables(source):
    with source.cursor() as cursor:
        cursor.execute("""
            SELECT table_name, engine, auto_increment
            FROM information_schema.tables WHERE table_schema = DATABASE()
            """)
        tables = {name: (engine, next_id) for name, engine, next_id in cursor}
    for table in TABLES:
        if table not in tables or tables[table][0] != "InnoDB":
            raise ValueError(f"MySQL 缺少 InnoDB 表 {table}")
    return tables


def table_columns(source, target, table):
    with source.cursor() as cursor:
        cursor.execute(f"SELECT * FROM {mysql_identifier(table)} LIMIT 0")
        source_columns = [column[0] for column in cursor.description]
    target_columns = [STAT_COLUMNS.get(column, column) for column in source_columns]
    result = target.execute("""
        SELECT column_name, is_identity FROM information_schema.columns
        WHERE table_schema = 'public' AND table_name = %s
        """, (table,))
    actual = dict(result.fetchall())
    if set(target_columns) != actual.keys() or actual.get("id") != "YES":
        raise ValueError(f"PostgreSQL 表 {table} 的列或 identity 定义不匹配")
    return source_columns, target_columns


def as_postgresql(row, first_flag):
    # MySQL datetime 不带时区，按 MYSQL_TIMEZONE 补上时区后写入 timestamptz。
    values = [
        value.replace(tzinfo=SOURCE_TIMEZONE) if type(value) is datetime and value.tzinfo is None else value
        for value in row
    ]
    if first_flag is not None and values[first_flag] is not None:
        if values[first_flag] not in (0, 1):
            raise ValueError("首访标记应为 0、1 或 NULL")
        values[first_flag] = bool(values[first_flag])
    return tuple(values)


def update_hash(digest, row):
    # 两端的 timestamptz 统一换算成 UTC 再比较，避免会话时区不同导致误报。
    values = tuple(
        value.astimezone(timezone.utc) if type(value) is datetime and value.tzinfo is not None else value
        for value in row
    )
    digest.update(repr(values).encode("utf-8") + b"\n")


def copy_and_verify(source, target, table, source_next_id, verify_only):
    source_columns, target_columns = table_columns(source, target, table)
    source_columns_sql = ", ".join(map(mysql_identifier, source_columns))
    target_table = sql.Identifier("public", table)
    target_columns_sql = sql.SQL(", ").join(map(sql.Identifier, target_columns))
    first_flag = source_columns.index("first_flag") if table == "t_link_access_logs" else None
    id_column = source_columns.index("id")
    source_digest, target_digest = sha256(), sha256()
    count = max_id = 0
    with source.cursor(pymysql.cursors.SSCursor) as source_rows, target.cursor() as writer:
        source_rows.execute(f"SELECT {source_columns_sql} FROM {mysql_identifier(table)} ORDER BY id")
        copy_statement = sql.SQL("COPY {} ({}) FROM STDIN").format(target_table, target_columns_sql)
        if verify_only:
            for row in source_rows:
                update_hash(source_digest, as_postgresql(row, first_flag))
                count += 1
        else:
            with writer.copy(copy_statement) as copy:
                for row in source_rows:
                    converted = as_postgresql(row, first_flag)
                    copy.write_row(converted)
                    update_hash(source_digest, converted)
                    count += 1
                    max_id = max(max_id, row[id_column])
    with target.cursor(name="verify_rows") as reader:
        query = sql.SQL("SELECT {} FROM {} ORDER BY id").format(target_columns_sql, target_table)
        reader.execute(query)
        target_count = 0
        for row in reader:
            update_hash(target_digest, row)
            target_count += 1
    if (count, source_digest.digest()) != (target_count, target_digest.digest()):
        raise ValueError(f"{table} 校验失败 (MySQL {count} 行, PostgreSQL {target_count} 行)")
    if not verify_only:
        next_id = max(max_id + 1, source_next_id or 1)
        statement = sql.SQL("ALTER TABLE {} ALTER COLUMN id RESTART WITH {}")
        target.execute(statement.format(target_table, sql.Literal(next_id)))
    print(f"{table}: {count} rows verified", flush=True)
    return count


def migrate(source, target, verify_only):
    with source.cursor() as cursor:
        cursor.execute("SET TRANSACTION ISOLATION LEVEL REPEATABLE READ")
        cursor.execute("START TRANSACTION WITH CONSISTENT SNAPSHOT, READ ONLY")
    tables = source_tables(source)
    identifiers = [sql.Identifier("public", table) for table in TABLES]
    lock_mode = sql.SQL("SHARE" if verify_only else "ACCESS EXCLUSIVE")
    target.execute("SET LOCAL lock_timeout = '5s'")
    target.execute(sql.SQL("LOCK TABLE {} IN {} MODE").format(sql.SQL(", ").join(identifiers), lock_mode))
    if not verify_only:
        for table, identifier in zip(TABLES, identifiers, strict=True):
            if target.execute(sql.SQL("SELECT 1 FROM {} LIMIT 1").format(identifier)).fetchone():
                raise ValueError(f"PostgreSQL 表 {table} 已有记录，导入要求空库")
    total = 0
    for table in TABLES:
        try:
            total += copy_and_verify(source, target, table, tables[table][1], verify_only)
        except (pymysql.MySQLError, psycopg.Error) as exc:
            code = exc.sqlstate if isinstance(exc, psycopg.Error) else exc.args[0]
            raise ValueError(f"表 {table} 处理失败 ({type(exc).__name__}, {code})") from None
    target.commit()
    print(f"{len(TABLES)} tables, {total} rows verified")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--verify-only", action="store_true", help="compare both databases without importing")
    args = parser.parse_args()
    try:
        with closing(pymysql.connect(
            host=os.getenv("MYSQL_HOST", "127.0.0.1"),
            port=int(os.getenv("MYSQL_PORT", "3306")),
            user=os.getenv("MYSQL_USER", "linkapp"),
            password=os.environ["MYSQL_PASSWORD"],
            database=os.getenv("MYSQL_DATABASE", "db_shortlink"),
            charset="utf8mb4",
        )) as source, psycopg.connect(
            host=os.getenv("PGHOST", "127.0.0.1"),
            port=int(os.getenv("PGPORT", "5432")),
            user=os.getenv("PGUSER", "linkapp"),
            password=os.environ["PGPASSWORD"],
            dbname=os.getenv("PGDATABASE", "db_shortlink"),
        ) as target:
            migrate(source, target, args.verify_only)
    except (KeyError, ValueError, psycopg.Error, pymysql.MySQLError) as exc:
        print(f"迁移或校验失败: {exc}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
