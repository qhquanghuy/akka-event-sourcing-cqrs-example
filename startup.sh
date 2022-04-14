#!/bin/bash

docker-compose up -d write_db
docker-compose up -d read_db

echo "WAIT 10s FOR DB IS INITIALIZED"
sleep 10
docker-compose run --rm flyway_write_db
docker-compose run --rm flyway_read_db

docker-compose up app