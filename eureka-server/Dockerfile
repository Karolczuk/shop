FROM openjdk:11-jdk-oracle
EXPOSE 8761
WORKDIR ./usr/app
ADD ./target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]