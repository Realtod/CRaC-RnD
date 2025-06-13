#!/usr/bin/env bash
set -e

SNAPSHOT_DIR=/app/cr

if [[ ! -d "$SNAPSHOT_DIR" || -z "$(ls -A "$SNAPSHOT_DIR")" ]]; then
  echo "🟠 Checkpoint отсутствует – создаю..."
  mkdir -p "$SNAPSHOT_DIR"

  MAKE_EARLY_CHECKPOINT=true \
  exec /opt/crac-jdk/bin/java \
       -XX:CRaCCheckpointTo=$SNAPSHOT_DIR \
       -jar spring-petclinic.jar
else
  echo "🟢 Checkpoint найден – быстрый старт!"
  exec /opt/crac-jdk/bin/java \
       -XX:CRaCRestoreFrom=$SNAPSHOT_DIR \
       -jar spring-petclinic.jar
fi
