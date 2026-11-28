# 访问日志和首访表的 user 列改名为 visitor_id

## 背景

`t_link_access_logs` 和 `t_link_first_visit` 的 `"user"` 列保存的是浏览器 Cookie 里的访客标识，不是用户账号，
项目里真正的用户字段叫 `username`，两者容易混淆。`user` 还是 PostgreSQL 保留字，建表、`@TableField`、Mapper 里的原生 SQL 都要加双引号。
上一次提交已把 `LinkStatsRecordDTO.uv` 改名为 `visitorId`，这里让存储层的命名保持一致。

## 方案

- 两张表的 `"user"` 列改为 `visitor_id`，同步改注释、索引名和唯一约束名。
- 实体属性 `user` 改为 `visitorId`。`visitor_id` 不是保留字，MyBatis-Plus 的驼峰转下划线即可映射，`LinkAccessLogsDO` 上的 `@TableField("\"user\"")` 随之删除。
- `LinkStatsAccessRecordVO.user` 改为 `visitorId`。VO 由 `BeanUtil.toBean` 按属性名从实体复制，两者必须同名，否则访客标识会丢失。前端 `types.ts` 同步改名，页面代码未读取该字段。
- 迁移脚本 `scripts/migrate-mysql-to-postgresql.py` 的列名映射增加 `user` 到 `visitor_id`。MySQL 旧库 `link.sql` 是迁移来源，保持不变。
- 个人项目，没有需要保留的库，不写在线迁移步骤。已有库需要的话可手工执行：

```sql
BEGIN;
ALTER TABLE t_link_access_logs RENAME COLUMN "user" TO visitor_id;
ALTER TABLE t_link_first_visit RENAME COLUMN "user" TO visitor_id;
ALTER INDEX t_link_access_logs_idx_full_short_url_user RENAME TO t_link_access_logs_idx_full_short_url_visitor_id;
ALTER TABLE t_link_first_visit RENAME CONSTRAINT t_link_first_visit_uniq_url_user TO t_link_first_visit_uniq_url_visitor_id;
COMMIT;
```

## 影响范围

- 数据库：`link-postgresql.sql`（两张表的列、注释、索引、唯一约束）
- 实体和 VO：`LinkAccessLogsDO.kt`、`LinkFirstVisitDO.kt`、`LinkStatsAccessRecordVO.kt`
- Mapper：`LinkAccessLogsMapper.kt`（6 处）、`LinkFirstVisitMapper.kt`（`"user"` 列和 `#{user}` 参数）
- `LinkStatsSaver.java`：两处 builder 调用 `.user(...)` 改为 `.visitorId(...)`
- 前端：`frontend/src/api/types.ts`
- 脚本和文档：`scripts/migrate-mysql-to-postgresql.py`、`notebook/intro/ShortLink项目技术文档.md` 中的表结构说明
- 接口：访问记录接口返回的字段由 `user` 变为 `visitorId`。

## 测试

- 已用 grep 确认 `dao`、`vo`、`mq` 和建表 SQL 中没有残留的 `"user"` 列引用。
- 待补：用新建表 SQL 初始化一个空库，启动应用，走一遍"创建 → 跳转 → 统计"，
  确认访问日志写入、首访判重、UV 统计和访问记录列表（含 `visitorId` 字段）正常。
- 原生 SQL 和 `#{visitorId}` 参数在编译期不会被检查，必须靠运行验证。

## 遗留问题

- 迁移脚本的列名映射改动尚未用真实的 MySQL 数据验证。
