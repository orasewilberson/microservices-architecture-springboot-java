# Etape 1 : utiliser une image Java officielle
FROM openjdk:17-jdk-slim

# Etape 2 : définir le dossier de travail
WORKDIR /app

# Etape 3 : copier ton jar dans le conteneur
COPY target/*.jar app.jar

# Etape 4 : exposer le port de ton application
EXPOSE 9002

# Etape 5 : lancer ton application
ENTRYPOINT ["java", "-jar", "app.jar"]
