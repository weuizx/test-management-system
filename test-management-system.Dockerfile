FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/pineapple-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar /app/pineapple-0.0.1-SNAPSHOT.jar
