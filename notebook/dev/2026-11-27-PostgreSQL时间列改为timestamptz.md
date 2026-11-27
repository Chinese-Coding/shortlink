# PostgreSQL 时间列改为 timestamptz

## 背景

`link-postgresql.sql` 从 MySQL 的 `link.sql` 转换而来，时间列使用 `timestamp without time zone`，与 MySQL `datetime` 对应。该类型不记录时区，多时区部署或修改 JVM 时区后，新旧数据无法区分。PostgreSQL 推荐使用 `timestamptz`。同时清理了转换后遗留的冗余写法。

## 方案

- SQL 中 31 处时间列定义（`create_time`、`update_time`、`valid_date`，分表复制后共 106 列）统一改为 `timestamptz`。`stats_date` 是 `date` 类型，未改动。
- 删除全部冗余的 `DEFAULT NULL`，`DEFAULT '0'` 改为 `DEFAULT 0`。表结构、约束和索引的逻辑不变。
- `t_link_device_stats` 和 `t_link_network_stats` 的唯一约束名中沿用的 `browser` 改为 `device`、`network`。代码中没有按约束名引用的地方。
- Java 和 Kotlin 代码不需要改动。实体使用 `java.util.Date`，pgjdbc 写入时带偏移量，读取得到同一时刻。统计查询的日期参数是字符串，按会话时区解析，会话时区与 JVM 一致（`Asia/Shanghai`）。
- `scripts/migrate-mysql-to-postgresql.py`：
  - 源端不带时区的 `datetime` 按 `MYSQL_TIMEZONE`（默认 `Asia/Shanghai`）补上时区后写入。
  - 校验哈希前将两端的带时区时间统一换算为 UTC。
  - 表名提取修复：SQL 只显式创建 `*_0` 模板表，其余分表由 `DO` 块创建，原正则只识别 17 张表，会静默跳过 45 张分表的数据。现按 16 个分片展开为 62 张表。

## 影响范围

- 修改文件：`link-postgresql.sql`、`scripts/migrate-mysql-to-postgresql.py`、`README.md`（导入步骤增加 `MYSQL_TIMEZONE`）。
- 涉及数据库变更。新库直接使用新脚本。已有 PostgreSQL 库不会自动变更，需要对每个时间列执行 `ALTER TABLE ... ALTER COLUMN ... TYPE timestamptz USING col AT TIME ZONE 'Asia/Shanghai'`。
- 不涉及配置变更，`shardingsphere-config.yaml` 无需修改。

## 测试

用 Docker 的 `postgres:19.0-alpine` 和 `mysql:8.0` 验证：

- `psql -v ON_ERROR_STOP=1` 执行完整脚本成功，共 62 张表，106 个时间列均为 `timestamptz`。
- 分表 `t_link_15` 的字段注释、索引、默认值正确，`short_uri` 的 `COLLATE "C"` 区分大小写。
- 写入 `10:00+08`，在 UTC 会话中读出 `02:00`。
- 在 MySQL 导入 `link.sql` 并写入测试数据（含分表 `t_link_3`、`t_group_15`、`t_link_goto_9`），迁移和 `--verify-only` 均通过，共 62 张表、9 行。`first_flag` 的 1/0/NULL 转为 true/false/NULL，`date/hour/weekday` 正确映射到 `stats_*`，`datetime` 按上海时间换算为 UTC，identity 序列接在最大 id 之后。
- 修复表名提取前，同一份数据只迁移 6 行，分表数据缺失。

## 遗留问题

- 未启动 Java 应用，创建、跳转、统计的端到端流程尚未验证。
- 迁移只用 9 行数据验证，未测试大数据量和耗时。
- 迁移默认 MySQL 的 `datetime` 是上海时间，取决于原来 MySQL JDBC 的 `serverTimezone`。源库时区不同时必须设置 `MYSQL_TIMEZONE`，否则时间会整体偏移。
- `username`、`gid`、`full_short_url`、`message_id` 在 MySQL 默认排序规则下不区分大小写，在 PostgreSQL 中区分。迁移前如存在仅大小写不同的数据，行为会发生变化。
- `t_api_token.update_time` 原为 `ON UPDATE CURRENT_TIMESTAMP`，PostgreSQL 版依赖 MyBatis-Plus 的 `FieldFill.INSERT_UPDATE`，不经实体的更新不会刷新该列。
