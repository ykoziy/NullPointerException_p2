# FROM <image>:<tag>
FROM openjdk:8-jdk-alpine
# COPY source to destination
COPY back-end/target/back-end-0.0.1-SNAPSHOT.jar NullPointerExceptionStore.jar
EXPOSE 8080
CMD ["java", "-jar", "NullPointerExceptionStore.jar"]