# PostgreSQL 19 迁移

## 背景

在独立分支验证 PostgreSQL 作为短链服务的数据库。使用 PostgreSQL 19.0、Java 25 与 Redis 8.12.0。

## 方案

- 切换 PostgreSQL JDBC 驱动、ShardingSphere PostgreSQL 方言、MyBatis-Plus 分页和 Compose 初始化。保留单库、原分片键和各 16 张物理表，以及原有 AES 加密规则。
- `link-postgresql.sql` 初始化原有的 62 张表。使用 PostgreSQL identity 列、索引与类型，并保留 `link.sql` 作为 MySQL 表结构参考。
- 将统计累加和首次访问去重改成 `ON CONFLICT`。统计表的 `date`、`hour`、`weekday` 映射到 `stats_date`、`stats_hour`、`stats_weekday`，避开分片 SQL 解析时的关键字冲突。访问记录的 `first_flag` 改用布尔类型。
- 短链关键词搜索使用绑定参数和 `ILIKE`，保留原来的不区分大小写搜索。仍使用现有 API 时间字段，通过 PostgreSQL JDBC 参数类型推断支持统计日期字符串。

## 验证

- `mvn clean package` 通过，45 项测试成功。
- 初始化 PostgreSQL 19.0，验证注册、登录、创建短链、302 跳转、Redis Stream 落库、重复访问去重和统计记录分页。
- 将短链移动到不同分片的分组，验证跳转、关键词分页和继续累计访问量。
- 用应用镜像和 Docker 外置配置再次跑通主流程，并验证用户资料加解密及 API Token 创建、列表。

## 现有数据迁移

本次验证使用空库和测试数据。切换已有实例需要导入各物理表的数据，转换统计列名与首访标记，保留 AES 密钥，校准 identity 序列，并在数据副本上核对记录数、短链路由及历史统计。实际部署数据未迁移，性能尚未复测。
