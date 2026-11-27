# Docker 文件移入 docker 目录

## 背景

仓库根目录散放着 6 个 Docker 相关文件，和 `pom.xml`、`README.md` 等混在一起。统一放进 `docker/` 目录，根目录更清爽。

## 方案

移动的文件（均用 `git mv`，保留历史）：

| 原位置 | 新位置 |
|---|---|
| `Dockerfile` | `docker/Dockerfile` |
| `.dockerignore` | `docker/Dockerfile.dockerignore` |
| `docker-compose.yml` | `docker/docker-compose.yml` |
| `docker-entrypoint.sh` | `docker/docker-entrypoint.sh` |
| `application-docker.yaml` | `docker/application-docker.yaml` |
| `shardingsphere-config-docker.yaml` | `docker/shardingsphere-config-docker.yaml` |

关键决策：

- **构建上下文仍是仓库根目录**。镜像需要 `target/*.jar` 和 `src/main/resources/`，所以只用 `-f docker/Dockerfile` 指定 Dockerfile，上下文 `.` 不变。
- **`.dockerignore` 改名为 `Dockerfile.dockerignore`**。Dockerfile 不在上下文根目录时，BuildKit 在 Dockerfile 同目录查找 `<Dockerfile 名>.dockerignore`，根目录的 `.dockerignore` 不再生效。忽略项中的 Docker 文件改成新路径，且不忽略整个 `docker/`，因为 `Dockerfile` 需要 `COPY docker/docker-entrypoint.sh`。
- **`.env.example` 留在根目录**。本地开发也使用它。`docker compose` 默认从 compose 文件所在目录读取 `.env`，文件移走后根目录的 `.env` 会被静默忽略，密码等变量退回默认值 `YourStrongPassword`。因此启动命令统一为 `docker compose --env-file .env -f docker/docker-compose.yml up -d`。
- `link-postgresql.sql` 不移动。它不只用于 Docker，CLAUDE.md、开发者准则和迁移脚本都引用它。compose 中的挂载路径改为 `../link-postgresql.sql`。
- `Caddyfile` 和 `shortlink.nginx.conf` 是宿主机反向代理配置，不属于 Docker 部署，保持原位。

## 影响范围

- 修改文件：`docker/Dockerfile`（`COPY` 路径）、`docker/docker-compose.yml`（SQL 挂载路径）、`docker/Dockerfile.dockerignore`、`.github/workflows/docker-build.yml`（`file: ./docker/Dockerfile`）、`CLAUDE.md`、`README.md`、`notebook/project-analysis.md`、`notebook/intro/ShortLink项目技术文档.md`（路径和启动命令）。
- 不涉及数据库变更和应用配置变更。
- **使用方式变化**：原来的 `docker compose up -d` 和 `docker build -t ... .` 不能再直接用，需要按上面的新命令执行。已有的部署脚本或笔记里的旧命令要同步修改。

## 测试

- `docker compose --env-file .env.example -f docker/docker-compose.yml config` 解析通过，3 个挂载源文件均存在。
- `docker build -f docker/Dockerfile .` 构建成功，镜像内 `/docker-entrypoint.sh` 存在且可执行。
- 用探针 Dockerfile 对比：`docker/` 下有同名 `.dockerignore` 时 `*.md` 被排除，没有时可见，说明 `Dockerfile.dockerignore` 被 BuildKit 识别。
- 不带 `--env-file` 时 `POSTGRES_PASSWORD` 为默认值，确认了上面的坑。

## 遗留问题

- GitHub Actions 的 `docker-build.yml` 只改了 `file:` 路径，没有在 CI 上实际运行，需要合入后观察第一次构建。
- 经典构建器（`DOCKER_BUILDKIT=0`）不识别 `Dockerfile.dockerignore`。Docker 23 起默认使用 BuildKit，CI 使用 buildx，不受影响。
- `notebook/intro/ShortLink项目技术文档.md` 里仍有 MySQL、Redis 7.0 等过时描述，这次只改了路径和命令。
