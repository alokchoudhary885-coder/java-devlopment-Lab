#!/bin/sh
# ----------------------------------------------------------------------------
# ShopSphere Maven Wrapper Script for Unix / macOS / Linux
# ----------------------------------------------------------------------------

DIR="$(cd "$(dirname "$0")" && pwd)"

if [ -f "$DIR/.tools/maven/apache-maven-3.9.9/bin/mvn" ]; then
    exec "$DIR/.tools/maven/apache-maven-3.9.9/bin/mvn" "$@"
fi

if [ -f "$DIR/.mvn/wrapper/maven-wrapper.jar" ]; then
    exec java -jar "$DIR/.mvn/wrapper/maven-wrapper.jar" "$@"
fi

if command -v mvn >/dev/null 2>&1; then
    exec mvn "$@"
fi

echo "[ERROR] Maven or maven-wrapper.jar not found. Please install Maven or run setup-maven.ps1"
exit 1
