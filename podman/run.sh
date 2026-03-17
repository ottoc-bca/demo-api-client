#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

podman-compose -f "$SCRIPT_DIR/podman-compose.yml" up -d

echo "MySQL is starting on localhost:3306"
echo "  User:     root"
echo "  Password: root"
echo "  Database: devdb"
