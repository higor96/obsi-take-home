#!/usr/bin/env bash
# Builds the runnable JAR (target/obsi.jar) and runs the tests.
set -euo pipefail
cd "$(dirname "$0")"
./mvnw -B -ntp clean package "$@"
