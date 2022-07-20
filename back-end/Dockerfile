# FROM <image>:<tag>
FROM openjdk:8
# COPY source to destination
COPY target/back-end-0.0.1-SNAPSHOT.jar NullPointerExceptionStore.jar
EXPOSE 8080
CMD ["java", "-jar", "NullPointerExceptionStore.jar"]