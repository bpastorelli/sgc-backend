FROM ubuntu:latest
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y tzdata

FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S spring && adduser -S spring -G spring  
MAINTAINER Bruno Pastorelli
COPY target/sgc_producer.jar sgc_producer.jar
ENTRYPOINT ["java","-jar","/sgc_producer.jar"]
EXPOSE 9090