$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

podman-compose -f "$ScriptDir\podman-compose.yml" up -d

Write-Host "Services are starting:"
Write-Host ""
Write-Host "MySQL:"
Write-Host "  Host:     localhost:3306"
Write-Host "  User:     root"
Write-Host "  Password: root"
Write-Host "  Database: devdb"
Write-Host ""
Write-Host "RabbitMQ:"
Write-Host "  AMQP:       localhost:5672"
Write-Host "  Management: http://localhost:15672"
Write-Host "  User:       guest"
Write-Host "  Password:   guest"
