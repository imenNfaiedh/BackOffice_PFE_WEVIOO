# Étape 1 : Utiliser une image de base avec JDK
FROM openjdk:17-jdk-alpine

VOLUME /tmp

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar" , "app.jar"]
