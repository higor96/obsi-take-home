#!/usr/bin/env bash
# Runs the OBSI CLI. Usage: ./run.sh [--new-ped] <input.json>
# Run ./build.sh first. Paths are resolved from the caller's working directory.
set -euo pipefail
JAR="$(cd "$(dirname "$0")" && pwd)/target/obsi.jar"
if [[ ! -f "$JAR" ]]; then
  echo "missing $JAR, run ./build.sh first" >&2
  exit 2
fi
exec java -jar "$JAR" "$@"
