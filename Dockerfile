# Build stage
FROM eclipse-temurin:23 AS build
RUN apt-get update && apt-get install -y maven
COPY /src /home/app/src
COPY /pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Run stage
FROM openjdk:23
COPY --from=build /home/app/target/webid-0.0.1-SNAPSHOT.war /usr/src/webid/webid.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/src/webid/webid.war"]

