
# Users service

[![GitHub release](https://img.shields.io/github/release/sjexpos/ecommecomm-users-service.svg?style=plastic)](https://github.com/sjexpos/ecomm-users-service/releases/latest)
[![CI workflow](https://img.shields.io/github/actions/workflow/status/sjexpos/ecomm-users-service/ci.yaml?branch=main&label=ci&logo=github&style=plastic)](https://github.com/sjexpos/ecomm-users-service/actions?workflow=CI)
[![Codecov](https://img.shields.io/codecov/c/github/sjexpos/ecomm-users-service?logo=codecov&style=plastic)](https://codecov.io/gh/sjexpos/ecomm-users-service)
[![Issues](https://img.shields.io/github/issues-search/sjexpos/ecomm-users-service?query=is%3Aopen&label=issues&style=plastic)](https://github.com/sjexpos/ecomm-users-service/issues)
![Commits](https://img.shields.io/github/last-commit/sjexpos/ecomm-users-service?logo=github&style=plastic)


This microservice is responsible for ...

## Modules structure

This project has a hexagonal architecture and the modules are:

* **api** - interfaces which are implemented as controllers on **api-rest**
* **db-scripts** - flyway database scripts
* **domain** - domain objects
* **application** - use cases
* **infrastructure**
   * **api-rest** - controllers which implement interfaces on **api**
   * **persistence** - all classes and functionality related to how domain object are stored.
   * **spring-boot** - all classes and configuration related to IoC container (spring boot)
* **sdk** - classes which implement interfaces on module **api** to call services of this microservice remotely.
* **uploads-detector** - AWS lambda function which is triggered when a file is uploaded to S3 using URL signed, and it invokes an user service endpoint to notify this.

## Framework

* [Spring Boot 2.7](https://spring.io/projects/spring-boot/)
* [Spring Cloud](https://spring.io/projects/spring-cloud)
* [Spring Data 2.7](https://spring.io/projects/spring-data)
* [Spring Cache]()
* [Openapi V3](https://swagger.io/specification/)
* [Hibernate 5.6](https://hibernate.org/orm/)
* [Hibernate Search 6.1](https://hibernate.org/search/)

## Requirements

* [Java 11/17](https://openjdk.org/install/)
* [Maven 3.6+](https://maven.apache.org/download.cgi)
* [AWS Cli](https://aws.amazon.com/es/cli/)
* [Docker](https://www.docker.com/)

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

### Start volatile storage locally

```bash
cd infrastructure/spring-boot
mvn -Dtests.db.database=ecomm_users pre-integration-test
```

### Run application
```
./run.sh
```

### Debug application on port 5005
```
./debug.sh
```

## Swagger UI

http://localhost:8080/api/api-docs.html


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
