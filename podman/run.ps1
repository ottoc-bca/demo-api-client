$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

podman-compose -f "$ScriptDir\podman-compose.yml" up -d

Write-Host "MySQL is starting on localhost:3306"
Write-Host "  User:     root"
Write-Host "  Password: root"
Write-Host "  Database: devdb"
