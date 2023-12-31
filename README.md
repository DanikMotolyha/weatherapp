# Weather application

### [Task](/Task.txt)

### [Postman requests](/WeatherApp.postman_collection.json)

### [Create sql](/create.sql)

## Table of Contents

- [Interaction](#Interaction)
- [Getting Started](#getting-started)
- [Docker compose](#docker-compose)
  
### Requirements
* docker
* java 17

### Getting Started

1. Clone the repo

```sh
git clone https://github.com/DanikMotolyha/weatherapp.git
```

2. go to the project

```sh
cd weatherapp
```

3. use gradle to build Spring Boot Application

```sh
gradlew build
```

4. use docker-compose to run application

```sh
docker-compose up
```

### Interaction

Two endpoints:

#### 1. Give current weather info from database

```
curl -i localhost:8080/weather/current/
```

example response
Empty
```
HTTP/1.1 404
Content-Type: application/problem+json
Transfer-Encoding: chunked
Date: Thu, 30 Nov 2023 12:19:08 GMT

{
    "type":"about:blank",
    "title":"Not Found",
    "status":404,
    "detail":"Database is empty",
    "instance":"/weather/current/"
}
```
Data
```
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 30 Nov 2023 12:20:28 GMT

{
    "temperature":-6.0,
    "windSpeed":18.6,
    "atmosphericPressure":997.0,
    "humidity":86,
    "weatherConditions":100,
    "location":"Minsk"
}
```

#### 1. Give average daily weather info from database

```
curl -i "localhost:8080/weather/average/?from=2023-11-29&to=2023-11-30"
```

example response
Empty
```
HTTP/1.1 404
Content-Type: application/problem+json
Transfer-Encoding: chunked
Date: Thu, 30 Nov 2023 12:19:08 GMT

{
    "type":"about:blank",
    "title":"Not Found",
    "status":404,
    "detail":"Database is empty",
    "instance":"/weather/current/"
}
```
Data
```
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 30 Nov 2023 12:20:28 GMT

{
    "temperature":-6.0,
    "windSpeed":18.6,
    "atmosphericPressure":997.0,
    "humidity":86,
    "weatherConditions":100,
    "location":"Minsk"
}
```

### Docker-compose

```
services:

  db-postgres:
    container_name: weather-postgres
    image: postgres
    environment:
      POSTGRES_DB:        weather
      POSTGRES_USER:      root
      POSTGRES_PASSWORD:  root
    volumes:
      - db:/var/lib/postgresql/
    ports:
      - 5432:5432
  weatherapp:
    container_name: weather-api
    build: .
    ports:
      - 8080:8080
    environment:
      POSTGRES_USER: root
      POSTGRES_PASSWORD: root
      POSTGRES_URL: jdbc:postgresql://db-postgres:5432/weather
      SHCEDULED_UPDATE_WEATHER_INTERVAL: "0 */1 * * * *"
```

#### SHCEDULED_UPDATE_WEATHER_INTERVAL:

An environment variable that controls the weather update interval using cron format
