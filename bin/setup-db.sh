#!/usr/bin/env bash
set -euo pipefail

# Usage: bin/setup-db.sh [port]
PORT=${1:-5433}
DB_URL=jdbc:postgresql://localhost:${PORT}/dicegame

echo "Starting postgres via docker-compose..."
docker-compose up -d db

echo "Waiting for Postgres to be ready on port ${PORT}..."
until docker exec -i dice-postgres pg_isready -U postgres -d dicegame >/dev/null 2>&1; do
  sleep 1
done

echo "Applying SQL migrations..."
cat src/main/resources/db/migration/V1__create_tables.sql | docker exec -i dice-postgres psql -U postgres -d dicegame

echo "Done. Connection URL: ${DB_URL} (user=postgres, password=postgres)"

echo "You can set environment variables like:"
echo "  export DB_URL=${DB_URL}"
echo "  export DB_USER=postgres"
echo "  export DB_PASSWORD=postgres"
