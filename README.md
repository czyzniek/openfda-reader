# OpenFDA drug record application reader

Simple spring-boot based application that enables user to fetch drug record application from openFDA public servers.
Additionally, service enables user to save specific details of drug record applications. Please refer to OpenAPI
definition on how to use exposed API.

## How to build app

```shell
./gradlew clean build
```

## How to test app

```shell
./gradlew clean test
```

## How to run app locally

```shell
./gradlew clean bootRun
```

## How to build docker image locally

```shell
./gradlew jibDockerBuild
```

## Swagger and OpenAPIv3

To access locally swagger just go to:

```
http://localhost:8080/swagger-ui.html
```

To fetch current OpenAPIv3 definition just go to:

```
http://localhost:8080/v3/api-docs
```