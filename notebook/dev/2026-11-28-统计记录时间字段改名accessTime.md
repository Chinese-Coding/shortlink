# LinkStatsRecordDTO 字段改名：accessTime 和 visitorId

## 背景

- `currentDate`：在 `LinkServiceImpl` 中由 `new Date()` 赋值，记录的是用户访问短链的时刻。
  统计消息先进入 Redis Stream，之后才由消费者处理，"当前时间"容易被理解为消费处理时间。
  该字段含时分秒，`Date` 后缀也不准确。
- `uv`：在统计表和 VO 中 `uv` 是访客数（`Integer`），在 `LinkStatsRecordDTO` 中却是浏览器 Cookie 里的访客标识（`String`），
  同一个词两种含义，`statsRecord.getUv()` 和 `stats.getUv()` 容易混淆。

## 方案

- `currentDate` 改名为 `accessTime`，注释改为"访问时间"。
- `uv` 改名为 `visitorId`，注释改为"访客标识，取自浏览器 visitorId Cookie 中的 UUID"。
- 浏览器 Cookie 名由 `uv` 改为 `visitorId`，常量 `UV_COOKIE_MAX_AGE_SECONDS` 改为 `VISITOR_COOKIE_MAX_AGE_SECONDS`。
- `LinkStatsSaver.save` 入口处把 `accessTime` 转成 `Instant`，统一用 `Instant` 计算统计日期、小时和星期，仅写库的统计日期转回 `Date`。
- DTO 的 `accessTime` 仍是 `Date`，JSON 格式不变。
- 不兼容旧数据：`LinkStatsRecordDTO` 经 `JSON.toJSONString` 写入 Redis Stream，JSON key 变为 `accessTime` 和 `visitorId`，
  旧 Cookie `uv` 不再读取。个人项目，不考虑兼容。

## 影响范围

- `dto/biz/LinkStatsRecordDTO.kt`：属性、builder、`equals`/`hashCode`/`toString`
- `service/impl/LinkServiceImpl.java`：Cookie 的读写、构造 DTO 时的 `.accessTime(...)` 和 `.visitorId(...)`
- `mq/consumer/LinkStatsSaver.java`：读取新字段，改用 `Instant` 计算
- `common/constant/LinkConstant.kt`：Cookie 有效期常量改名
- `notebook/intro/ShortLink项目技术文档.md`：流程图中的 Cookie 名
- 本次不涉及数据库变更和配置变更（访问日志表的 `"user"` 列随后改为 `visitor_id`，见 `2026-11-28-访问日志user列改名visitor_id.md`）。

## 测试

- 已用 grep 确认源码中没有残留的 `currentDate` / `getCurrentDate` / `statsRecord.getUv()` 引用
  （`LinkServiceImpl` 更新有效期处的局部变量 `currentDate`、统计表和 VO 里的 `uv` 是另一个用途，未改动）。
- `mvn -q compile` 编译通过。待补：走一遍"创建 → 跳转 → 统计"的端到端测试，
  确认响应里出现 `visitorId` Cookie，访问日志和 UV 统计正常。

## 遗留问题

- 上线前需排空 Redis Stream 及 Pending 中的旧消息，否则旧消息反序列化后 `accessTime` 为 null
  （`LinkStatsSaver` 会回退为当前时间），`visitorId` 为空（UV 统计会漏计）。
- 访客标识列的改名已在后续提交完成，见 `2026-11-28-访问日志user列改名visitor_id.md`。
