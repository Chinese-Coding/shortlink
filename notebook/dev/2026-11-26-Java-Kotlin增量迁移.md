# Java 到 Kotlin 增量迁移

## 背景

在 ShortLink 中引入 Kotlin，并按包迁移现有 JVM 源码。Java 25 仍是构建和运行所用的 JDK。

## 方案

- Kotlin 版本与 Spring Boot 的依赖管理对齐为 2.4.21。Maven 在生产和测试阶段都先编译 Kotlin，再交给 javac 编译 Java。两种语言共用现有源码目录，Java 继续使用 Lombok 注解处理。
- 保留 Spring Boot 主类全限定名、MVC 路由及参数名、Spring Bean 名称、Mapper SQL 与参数绑定、Java 静态调用入口和实体访问器。
- 对 Java 仍在使用的 Builder 和无参构造保留对应入口。Jackson、Fastjson 和 MyBatis 的字段注解显式放在 JVM 字段上。对基于继承的 Bean 保留 Lombok 原有的相等性规则。

## 影响范围

163 个生产 Java 文件中的 133 个迁为 Kotlin，包括应用入口、服务接口、Mapper、实体、DTO、VO、常量、若干 Spring 配置、Web 组件和 Redis Stream 组件。20 个测试文件中有 3 个测试类迁为 Kotlin，并为高德客户端新增成功响应绑定用例。迁移涉及 `pom.xml` 和 JVM 源码，无数据库结构或部署配置变更。

## 验证

- 迁移前用 JDK 25 和 Maven 验证 45 个测试通过。迁移后执行 `mvn clean package`，46 个测试全部通过，并生成可执行 JAR。
- 用 `javap` 对照迁移前后原有公开和受保护成员的 JVM 描述符、静态性与受检异常声明。
- 用隔离的 PostgreSQL 19 和 Redis 8.12 容器启动 JAR，验证注册登录、创建短链、302 跳转、异步统计、分页边界、分组排序、Token JSON、回收和恢复。

## 遗留问题

服务实现、认证过滤器、部分 Redis Stream 消费逻辑、短码算法和 AI/MCP 仍有 Java 源码。端到端验证使用了关闭 AI 的配置，未请求外部模型。
