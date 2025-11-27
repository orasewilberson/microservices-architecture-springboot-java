# Architecture Microservices avec Spring Boot

## Description
Ce projet est une démonstration d'une **architecture microservices** implémentée avec **Spring Boot** ce n'est pas un projet totalement finalisé.  
Il inclut plusieurs microservices qui communiquent entre eux ainsi qu'une application web pour l'interface utilisateur.  
L'architecture est modulable, évolutive et conçue pour être maintenable facilement.



## Structure du projet

| Module / Service        | Description |
|-------------------------|-------------|
| `api`                   | API centrale qui communique avec les autres microservices. Gère l'authentification et la logique métier principale. |
| `webapp`                | Application frontend (Spring Boot + Thymeleaf) pour interagir avec les microservices. |
| `employee-service`      | Microservice pour gérer les employés. |
| `departement-service`   | Microservice pour gérer les départements. |
| `gateway-service`       | API Gateway (Spring Cloud Gateway) pour le routage et l’équilibrage de charge. |
| `discovery-server`      | Serveur Eureka pour l’enregistrement et la découverte des services. |
| `docker-compose.yml`    | Fichier Docker Compose pour exécuter tous les services ensemble. |



## Fonctionnalités
- **Authentification et autorisation des utilisateurs**  
- **CRUD pour les employés**  
- **CRUD pour les départements**  
- **Découverte des services avec Eureka**  
- **Routage via l’API Gateway**  
- **Intégration avec l’application web frontend**  


## Prérequis
Avant de lancer le projet, assurez-vous d’avoir installé :  
- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
- [Maven](https://maven.apache.org/install.html)  
- [Docker & Docker Compose](https://docs.docker.com/get-docker/) (optionnel pour exécuter tous les services ensemble)  


## Démarrage

## Exécution des services individuellement
1. Aller dans le dossier du microservice (ex : `employee-service`)  
2. Lancer le service avec Maven :

mvn spring-boot:run


## Exécution de tous les services avec Docker Compose
docker-compose up --build

## Accéder aux applications
- API Gateway : http://localhost:9001
- Webapp : http://localhost:9002 

## Configuration
Les fichiers application.properties contiennent les configurations propres à chaque service.


## Auteur

- Wilberson Orase developpeur
- GitHub : https://github.com/orasewilberson
- Email : orasewilberson@gmail.com
