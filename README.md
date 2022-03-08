# Spring Boot Microservices

The backend for a website that finds restaurants around a particular location 
and allows users to post anonymous reviews. It also has a service that retrieves jokes.
Uses third party APIs to get location and restaurant data.

### Tech Stack
- Java Spring Boot
- Spring-Cloud Gateway
- Maven
- Docker

### Build and run tests
```mvn clean install```

### Run with docker

- Subscribe to relevant third party APIs and get an API key from [RapidApi](https://rapidapi.com/)
- Run postgres instance locally  or in a separate docker container
- Set up an ```.env``` file with all the environment variables (see ```sample.env```)
- ```docker-compose build && docker-compose up```