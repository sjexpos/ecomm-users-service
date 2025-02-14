
# Users service

[![GitHub release](https://img.shields.io/github/release/sjexpos/ecomm-users-service.svg?style=plastic)](https://github.com/sjexpos/ecomm-users-service/releases/latest)
[![CI workflow](https://img.shields.io/github/actions/workflow/status/sjexpos/ecomm-users-service/ci.yaml?branch=main&label=ci&logo=github&style=plastic)](https://github.com/sjexpos/ecomm-users-service/actions?workflow=CI)
[![Codecov](https://img.shields.io/codecov/c/github/sjexpos/ecomm-users-service?logo=codecov&style=plastic)](https://codecov.io/gh/sjexpos/ecomm-users-service)
[![Issues](https://img.shields.io/github/issues-search/sjexpos/ecomm-users-service?query=is%3Aopen&label=issues&style=plastic)](https://github.com/sjexpos/ecomm-users-service/issues)
[![Commits](https://img.shields.io/github/last-commit/sjexpos/ecomm-users-service?logo=github&style=plastic)](https://github.com/sjexpos/ecomm-users-service/commits)

[![Docker pulls](https://img.shields.io/docker/pulls/sjexposecomm/users-service?logo=docker&style=plastic)](https://hub.docker.com/r/sjexposecomm/users-service)
[![Docker size](https://img.shields.io/docker/image-size/sjexposecomm/users-service?logo=docker&style=plastic)](https://hub.docker.com/r/sjexposecomm/users-service/tags)

![](docs/images/arch-users.png)

This microservice is responsible for ...


## Modules structure

This project has a hexagonal architecture and the modules are:

* **api** - interfaces which are implemented as controllers on **rest-api**
* **db-scripts** - flyway database scripts
* **domain** - domain objects
* **application** - use cases
* **infrastructure**
   * **rest-api** - controllers which implement interfaces on **api**
   * **persistence** - all classes and functionality related to how domain object are stored.
   * **spring-boot** - all classes and configuration related to IoC container (spring boot)
* **sdk** - classes which implement interfaces on module **api** to call services of this microservice remotely.
* **uploads-detector** - AWS lambda function which is triggered when a file is uploaded to S3 using URL signed, and it invokes an user service endpoint to notify this.

## Framework

* [Spring Boot 3.3.2](https://spring.io/projects/spring-boot/)
* [Spring Cloud 2023.0.3](https://spring.io/projects/spring-cloud)
* [Spring Data 3.3.2](https://spring.io/projects/spring-data)
* [Openapi V3](https://swagger.io/specification/)
* [Hibernate 6.6](https://hibernate.org/orm/)
* [Hibernate Search 7.2.0](https://hibernate.org/search/)

## Observability

This project implements observability using [micrometer](https://micrometer.io/)

* traces: they are exported using micrometer but [OpenTelemetry](https://opentelemetry.io) protocol (otlp) is used. The receiver of the information is [Jaeger](https://www.jaegertracing.io/). In kubernetes, jaeger collector is used. 
* metrics: they are pulled by [Prometheus](https://prometheus.io/). Metrics are exposed by Spring Boot Actuators. In kubernetes, a service monitor is used
* logs: they are sent to console. In kubernetes, Loki pulls logs from pods.

![](docs/images/observability-micrometer.png)

## Requirements

* [Java 21](https://openjdk.org/install/)
* [Maven 3.8.8+](https://maven.apache.org/download.cgi)
* [AWS Cli](https://aws.amazon.com/es/cli/)
* [Docker](https://www.docker.com/)

## Check styling

### Check if styling is applied

```bash
mvn spotless:check 
```

### Apply checkstyle to all files

```bash
mvn spotless:apply 
```

*Note*: this goal is run as part as build process

## Build

```bash
mvn clean && mvn install
```

## Run Tests
```bash
mvn clean && mvn tests
```

## Runtime requeriments

* **Postgres Database** - it must be run on port 5432. It must exist a schema named `ecomm_users` and schema owner must be `users_service`/`1234`.
* **Redis** - it must be run on port 6379. None password.
* **Opensearch** - it must be run on port 9200. None password.
* **AWS S3 bucket** - AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY which have access to a S3 bucket
* **[Localstack](https://github.com/localstack/localstack)** - it must be run on port 4566 for testing.
* **Kafka** - it must be run on port 9092.

### Start volatile storage locally

```bash
cd infrastructure/spring-boot
mvn -Dtests.db.database=ecomm_users pre-integration-test
```

### Run application
```bash
./run.sh
```

### Debug application on port 5005
```bash
./debug.sh
```

## Swagger UI

http://localhost:6061/


## Run application from IDE

This application uses S3Client from AWS SDK v2 because it needs to reach S3 and get a signed upload URL when a user wants to upload an avatar. So when it run locally and AWS credentials are not available, it's possible connect to localstack which is running as part as `local volatile storage`.

For that, the following environment variables must be defined in the IDE launcher
```
AWS_REGION=us-east-1
AWS_DEFAULT_REGION=us-east-1
AWS_ACCESS_KEY_ID=test
AWS_SECRET_ACCESS_KEY=test
AWS_ENDPOINT=http://localhost:4566
```


## Check message on a Kafka topic

It's possible to jump into kafka server using ssh, or connect to the container if it is running in a docker container,
and the following command will allow you to see the message which are generated by this service on topic "users-entities-update-topic"

```bash
/bin/kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic users-entities-update-topic
```
