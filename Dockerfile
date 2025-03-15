# Build stage
FROM maven:3.9.9-openjdk-23 AS build
WORKDIR /home/webid
COPY /src /home/webid/src
COPY /pom.xml /home/webid
RUN mvn clean package

# Run stage
FROM openjdk:23
COPY --from=build /home/webid/target/webid-0.0.1-SNAPSHOT.war webid.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/src/webid/webid.war"]

