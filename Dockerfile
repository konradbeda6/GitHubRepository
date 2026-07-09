FROM amazoncorretto:21
MAINTAINER KonradB
COPY target/Repositories-0.0.1-SNAPSHOT.jar repositories-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/repositories-0.0.1-SNAPSHOT.jar"]