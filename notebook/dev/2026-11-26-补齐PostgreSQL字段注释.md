# 补齐 PostgreSQL 字段注释

## 背景

PostgreSQL 初始化脚本缺少原 MySQL 建表脚本中的字段说明。

## 方案与影响范围

在 `link-postgresql.sql` 的各表定义后添加 `COMMENT ON COLUMN`。统计字段按 PostgreSQL 列名对应到原说明，布尔类型的首访标记使用 `true` 和 `false` 表述。三个分表模板在创建其余分表前写入注释，`LIKE ... INCLUDING ALL` 将注释一并复制。完整表定义、字段注释和索引写在文件前面，在 `COMMIT` 前使用一个 `DO` 循环，按编号 1 到 15 从三个模板创建 45 张分表。15 处唯一索引声明改为表内具名 `UNIQUE` 约束，PostgreSQL 自动创建对应的唯一索引。普通索引仍用独立的 `CREATE INDEX` 声明。

## 验证

在隔离的 PostgreSQL 19 容器中通过 `psql -v ON_ERROR_STOP=1` 执行完整脚本，并查询系统目录。共创建 62 张表，614 个字段均有注释。对比调整前后的 PostgreSQL 系统目录，128 个索引的键、访问方法、唯一性和 NULL 处理一致。

## 遗留问题

已有数据库需要单独执行字段注释语句才会更新数据库中的注释。
