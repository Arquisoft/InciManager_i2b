[![Codacy Badge](https://api.codacy.com/project/badge/Grade/386bd76d161c4783955d79cac8d10f85)](https://www.codacy.com/app/alejgh/InciManager_i2b?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Arquisoft/InciManager_i2b&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/Arquisoft/InciManager_i2b.svg?branch=master)](https://travis-ci.org/Arquisoft/InciManager_i2b)
[![CircleCI](https://circleci.com/gh/Arquisoft/InciManager_i2b.svg?style=svg)](https://circleci.com/gh/Arquisoft/InciManager_i2b)
[![codecov](https://codecov.io/gh/Arquisoft/InciManager_i2b/branch/master/graph/badge.svg)](https://codecov.io/gh/Arquisoft/InciManager_i2b)


# InciManager_i2b

## Authors
- Alejandro González Hevia (@alejgh)
- Carmen Sors González (@carmee-en)
- Alvaro Tejido Jardón (@tejido18)
- Eduardo Ulibarri Toledo (@uo251436)

## How to execute the module
There are two different possibilities in order to run the Incident Manager.

### Using Docker (recommended)
In order to run the module using Docker you just need to install just one dependency: Docker itself. You can download Docker from their [official site](https://www.docker.com/community-edition#/download).

Once you installed docker you have to start the Docker daemon and open a new command line if you are on Mac or Linux, or just open the Docker quickstart terminal if you are on Windows. After that, you execute `docker-compose up` inside the root directory of the Manager, where the docker-compose.yml file is located.

Once you execute this command, Docker Compose will launch three containers:
* A container running zookeper on port 2181.
* A container running kafka on port 9092.
* A container running the Incidence Manager application, listening on port 8001.

![Running docker-compose up](img/docker-start.png)

The first time you run docker-compose up will take quite a bit of time, because it needs to pull several images in order to run the application (zookeper, kafka and maven). Once the images are pulled they are cached by docker, so the next time you run the application it won't download the images again, taking less than a minute to start all the containers.

Once the application is running you can connect to it using http://localhost:8001 if you are running on Mac or Linux. If you are running it on Windows you will have to use the private IP specified when you start running Docker:
![Private ip windows](img/ip-windows.jpg)

Finally, when you are done using the application, you can use Ctrl+C to gracefully stop all the containers run by Docker:
![Stop containers](img/docker-stop.png)

### Without using Docker
If you want to run the Incidence Manager without using Docker you will need to setup Kafka and Zookeper manually, as well as running the spring boot application using Maven.

#### Kafka
First of all, you need to download Apache Kafka.

---tar -xzf kafka_2.11-1.0.1.tgz

---cd kafka_2.11-1.0.1

Before starting Apache Kafka, as it uses Zookeeper, you need to first start a ZooKeeper server if you don't already have one. You can use a script packaged with kafka to get a quick single-node ZooKeeper instance.

---bin\windows\zookeeper-server-start.bat config/zookeeper.properties

Now, you can start the Kafka server.

---bin\windows\kafka-server-start.bat config/server.properties

#### Mongo DB
This is the database of the system, to run it you have to go to the folder where you downloaded it and then run it.

---bin/mongod —port 27018

#### Maven
To run the application with maven, you have to run Apache Kafka and Mongo DB before. Then you have to go to the projects folder(where you have the pom.xml file) and type:

---mvn spring-boot:run

## Posting a new Incident
Once you have the Incident Manager application up and running you can create a new Incident by sending a POST request to http://localhost:8081/incident/create (if you are executing the application with Docker, use port 8001 instead). The format of the incident must be like this one:.
```json
{
  "username": "pruebas",
  "password": "cocoKiko",
  "kind": "Person",
  "inciName": "Primera Incidencia",
  "location": {
    "lat": -25.2,
    "lon": 56.1,
  },
  "tags": ["fire", "Person"],
  "moreInfo": ["myImage.jpg"],
  "properties": {
    "priority": 1,
    "temperature": "50ºC",
    "operators": ["Paco", "Carmen"]
  }
}
```
Inside the properties JSON object you can include any object you want.

You will need to have the [Agents_i2b module](https://github.com/Arquisoft/Agents_i2b) running in order to create the Incident, since the system has to check if the Agent exists before creating the incident.

## Querying the incidents of an agent
TODO

## Tests
TODO
