# Home Energy Tracker

A Spring Boot microservices system (Spring Boot 4.1, Java 26) that ingests and
processes home energy-usage telemetry.

```
                       ┌────────────────────┐
  HTTP  ─────────────▶ │ ingestion-service  │ ──▶ Kafka topic "energy-usage"
  (mock simulators)    │      :8082         │              │
                       └────────────────────┘              ▼
                                                    EnergyUsageConsumer
  ┌────────────────────┐        ┌────────────────────┐
  │ user-service :8080 │        │ device-service:8081│
  └─────────┬──────────┘        └─────────┬──────────┘
            ▼                            ▼
  home_energy_tracker_user     home_energy_tracker_device   (MySQL :3306)
```

Each service owns its own database schema (and its own Flyway history).

## Services

| Service           | Port | Responsibility                              | API                          |
|-------------------|------|---------------------------------------------|------------------------------|
| user-service      | 8080 | User CRUD, alerting preferences             | `/api/v1/user`               |
| device-service    | 8081 | Device CRUD per user                        | `/api/v1/device`             |
| ingestion-service | 8082 | Accept telemetry, publish to Kafka          | `/api/v1/ingestion`          |

Infrastructure (via `docker compose`): MySQL (`:3306`), Kafka KRaft (`:9094` from host,
`:9092` inside Docker), kafka-ui (`:8070`).

Every service exposes actuator (`/actuator/health`, `/actuator/metrics`, `/actuator/info`)
and Swagger UI (`/swagger-ui.html`).

## Prerequisites

- Java 26
- Docker + Docker Compose

## Getting Started

1. Start the infrastructure (from the project root):

   ```
   docker compose up -d
   ```

   To stop it:

   ```
   docker compose down
   ```

   MySQL data is kept in the `db-data` volume. If you hit schema/Flyway errors after
   pulling schema changes, reset it with `docker compose down -v` and start again
   (this recreates both databases from `docker/mysql/init.sql`).

2. Start each service (in its own terminal, from the project root):

   ```
   cd user-service      && ./mvnw spring-boot:run
   cd device-service    && ./mvnw spring-boot:run
   cd ingestion-service && ./mvnw spring-boot:run
   ```

   (On Windows use `mvnw.cmd` instead of `./mvnw`.)

## Data simulation

`ingestion-service` ships with two load generators that POST mock readings to its own
ingestion endpoint. Only one runs at a time — controlled in
`ingestion-service/src/main/resources/application.properties`:

```
simulation.parallel-enabled=true      # multi-threaded generator (default)
simulation.continuous-enabled=false   # single-threaded generator
simulation.requests-per-interval=1000 # requests per 5s tick
simulation.interval-ms=5000
simulation.parallel-threads=10
```

Set both to `false` to disable load generation entirely.

## Kafka

- Bootstrap (from the host): `localhost:9094`
- Topic: `energy-usage` (auto-created), events keyed by `deviceId`
- UI: http://localhost:8070
- Producer and consumer use JSON serialization; the consumer lives in
  `ingestion-service` (`EnergyUsageConsumer`) and currently logs events.
