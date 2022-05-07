FROM ubuntu:latest
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y tzdata

FROM java:11-jdk-alpine
MAINTAINER Bruno Pastorelli
COPY target/sgc_producer.jar /sgc_producer.jar
ENTRYPOINT ["java","-jar","/sgc_producer.jar"]
EXPOSE 9090