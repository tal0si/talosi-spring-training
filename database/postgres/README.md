# Talosi Training Sprint Database

## Requirements:
* docker >= 17.12.0+
* docker-compose

## Quick Start
* Clone the snippet
* Run this command `docker-compose up -d`


## Environments Variables
This Compose file contains the following environment variables:

* `POSTGRES_USER` the default value is **postgres**
* `POSTGRES_PASSWORD` the default value is **changeme**
* `PGADMIN_PORT` the default value is **5050**
* `PGADMIN_DEFAULT_EMAIL` the default value is **pgadmin4@pgadmin.org**
* `PGADMIN_DEFAULT_PASSWORD` the default value is **admin**

## Postgres database:
* `localhost:5432`
* **Username:** postgres (as a default)
* **Password:** changeme (as a default)

## PgAdmon:
* **URL:** `http://localhost:5050`
* **Username:** pgadmin4@pgadmin.org (as a default)
* **Password:** admin (as a default)

## Add a new server in PgAdmin:
* **Host name/address** `training-spring-postgres`
* **Port** `5432`
* **Username** as `POSTGRES_USER`, by default: `postgres`
* **Password** as `POSTGRES_PASSWORD`, by default `changeme`
