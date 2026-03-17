#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

podman-compose -f "$SCRIPT_DIR/podman-compose.yml" up -d

echo "Services are starting:"
echo ""
echo "MySQL:"
echo "  Host:     localhost:3306"
echo "  User:     root"
echo "  Password: root"
echo "  Database: devdb"
echo ""
echo "RabbitMQ:"
echo "  AMQP:       localhost:5672"
echo "  Management: http://localhost:15672"
echo "  User:       guest"
echo "  Password:   guest"
