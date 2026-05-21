# Dapr Workflows tracing demo (Java)

Demonstrates end-to-end distributed tracing across a Spring Boot 4 Dapr Workflow application using the OpenTelemetry Java agent. Traces can be exported either to a local OTLP collector (Dapr OSS) or to a Dash0 backend through a Diagrid Catalyst project. Useful for developers who want to observe workflow and activity spans alongside Dapr sidecar telemetry.

## Prerequisites

- Java 21
- Maven (the project includes the Maven Wrapper, `./mvnw`)
- Dapr CLI installed and initialized (`dapr init`) for the local-OTel path, or the Diagrid CLI logged in to a Catalyst project for the Catalyst path
- An OTLP-compatible backend (the included scripts target Dash0; substitute your own endpoint if needed)
- `OTEL_AUTH_TOKEN` exported as `Bearer <your-token>` before running either script

## Run locally

1. Build the project:
   ```
   ./mvnw package
   ```
2. Run against a local Dapr sidecar with OTel auto-instrumentation:
   ```
   export OTEL_AUTH_TOKEN="Bearer <your-token>"
   ./run-with-otel.sh
   ```
3. Or run against a Diagrid Catalyst project:
   ```
   export OTEL_AUTH_TOKEN="Bearer <your-token>"
   ./run-with-otel-catalyst.sh
   ```
4. Trigger a workflow via the app's REST endpoints and check your tracing backend (Dash0 by default) for the resulting spans.

## Project structure

- `pom.xml` — Spring Boot 4.0.3 + `dapr-spring-boot-4-starter` 1.17.0-rc-4 with the Spring Boot OpenTelemetry starter and the OTel Logback appender.
- `src/main/java/...` — workflow, activity, and controller sources.
- `src/main/resources/application.properties`, `logback-spring.xml` — Spring and logging configuration.
- `run-with-otel.sh` — runs locally via `mvn spring-boot:test-run` with the OpenTelemetry Java agent (v2.14.0) attached.
- `run-with-otel-catalyst.sh` — same, but wrapped in `diagrid dev run` so the app connects to a Catalyst project.

## Related

- [Dapr observability docs](https://docs.dapr.io/operations/observability/)
- [OpenTelemetry Java agent](https://github.com/open-telemetry/opentelemetry-java-instrumentation)

---

Join the [Dapr Discord](https://diagrid.ws/dapr-discord) for Q&A and chat with other community members!
