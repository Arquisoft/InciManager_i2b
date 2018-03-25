FROM maven:3.5-jdk-8-alpine
MAINTAINER Alejandro Gonzalez Hevia <alejandrgh11@gmail.com>
WORKDIR /usr/src/InciManager_i2b
COPY . /usr/src/InciManager_i2b/
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8081
CMD ["java", "-jar", "target/InciManager_i2b-0.0.1-SNAPSHOT.jar", "--spring.kafka.bootstrap-servers=kafka:9092", "--agents_url=http://agents:8080/agent", "--spring.data.mongodb.host=mongo_incidents"]
