# Poke-Poke

## Software architecture
This project is structured with an emphasis on **domain-driven design** and **separation of concerns**. 
The architecture focuses on keeping business logic at the core while ensuring clear boundaries 
between operations that produce side effects and those that are side-effect-free. 
Inspired to CQRS model, so the read model allows to decouple external Pokemon Api responses
from internal representation of pokemon read model.

![Example](docs/readmodel.png)

## Tech stack
- Kotlin (functional enrichment with arrowkt)
- Gradle as build tool
- Spring boot to provide web service functionality

## Run Service
The repository automatically build a docker image of microservice
```
petretiandrea/poke-poke
```
You can run the service using docker-compose file, from repository root folder
```
docker compose up -d
```
The service will run on 8080 port

## Build
- Docker 
The repository contains Dockerfile to build microservice image.
From root folder:
```
docker run -t <any_name> .
```

- Gradle to build project
```
gradlew :build
```
