#!/bin/bash
set -e

OTEL_AGENT_VERSION="2.14.0"
OTEL_AGENT_JAR="opentelemetry-javaagent.jar"
OTEL_AGENT_URL="https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/${OTEL_AGENT_JAR}"

# Download the OpenTelemetry Java agent if not already present
if [ ! -f "${OTEL_AGENT_JAR}" ]; then
  echo "Downloading OpenTelemetry Java agent v${OTEL_AGENT_VERSION}..."
  curl -sSL -o "${OTEL_AGENT_JAR}" "${OTEL_AGENT_URL}"
  echo "Download complete."
fi

# Check that OTEL_AUTH_TOKEN is set
if [ -z "${OTEL_AUTH_TOKEN}" ]; then
  echo "WARNING: OTEL_AUTH_TOKEN is not set. Export it before running this script:"
  echo "  export OTEL_AUTH_TOKEN=\"Bearer <your-token>\""
fi

OTEL_ENDPOINT="https://ingress.eu-west-1.aws.dash0.com"

# Run with auto-instrumentation
diagrid dev run --project spring-boot --app-id workflows --app-port 8080 -- mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="\
    -javaagent:$(pwd)/${OTEL_AGENT_JAR} \
    -Dotel.service.name=workflows \
    -Dotel.exporter.otlp.endpoint=${OTEL_ENDPOINT} \
    -Dotel.exporter.otlp.headers=Authorization='${OTEL_AUTH_TOKEN}',Dash0-Dataset=default \
    -Dotel.exporter.otlp.protocol=http/protobuf"
