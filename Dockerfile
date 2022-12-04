FROM openjdk:17
COPY target/university-0.0.1-SNAPSHOT.jar university-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/university-0.0.1-SNAPSHOT.jar"]