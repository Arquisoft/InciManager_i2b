FROM openjdk:8-jre-alpine
COPY ./target/InciManager_i2b-0.0.1-SNAPSHOT.jar /usr/src/InciManager_i2b/
WORKDIR /usr/src/InciManager_i2b
EXPOSE 8081
CMD ["java", "-jar", "InciManager_i2b-0.0.1-SNAPSHOT.jar"]
