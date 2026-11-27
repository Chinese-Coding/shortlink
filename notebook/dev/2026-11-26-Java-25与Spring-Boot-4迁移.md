# Java 25 与 Spring Boot 4.2 迁移

## 背景

项目从 Java 21 和 Spring Boot 3.1.10 升级至 Java 25 和 Spring Boot 4.2.0。Maven 编译目标、GitHub Actions 和 Docker 运行时统一使用 Java 25。

## 方案

- 使用 Spring Boot 4 的 Web MVC Starter。显式启用 Jackson 2 HTTP 转换器，兼容既有的日期格式、长整型 ID 字符串和脱敏序列化规则。MCP 和 AI 控制器继续使用 Jackson 2。
- 使用 MyBatis-Plus 的 Boot 4 Starter 和 JSqlParser 分页模块，更新 `ServiceImpl` 的导入。同步升级 springdoc 和 Redisson。
- ShardingSphere 5.3.2 的 YAML 解析器依赖旧版 SnakeYAML API。升级到 ShardingSphere 5.5.3，并显式引入 MySQL 解析、分片、加密、单机模式和 YAML 加载模块。用 `autoTables` 声明原有的三组 16 表分片，使用 `!SINGLE` 路由其余表。加密规则继续使用原有物理列及 AES 密钥，并显式选择与旧版本一致的 SHA-1 密钥摘要。
- 显式启用 Lombok 注解处理，并使用支持 JUnit Platform 的 Surefire。容器镜像工作流在发布前运行测试。

## 影响范围

已有 MySQL 表及数据无需迁移。部署时如挂载自定义 YAML，需按 README 更新配置格式。

## 验证

- 在 JDK 25.0.5.1 和 Maven 3.9.12 上执行 `mvn clean package`，45 项测试全部通过。
- 使用 MySQL 8.0 和 Redis 7.0 验证注册登录、短链创建与分页、HTTP 302、Redis Stream 访问统计、OpenAPI 和 MCP SSE。
- 通过 ShardingSphere 5.3.2 写入加密用户和分组，再用新版本登录、解密读取手机号和邮箱，并查询已有分组。
- 构建 Temurin 25 镜像，使用 Docker 外置配置启动应用并再次跑通端到端验证。

## 遗留验证

AI 工具注册已通过应用启动验证。真实模型调用和性能复测未覆盖。
