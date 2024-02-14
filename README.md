# Yoga Project

Ce projet a été généré avec [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0 pour le front-end et [Spring Boot](https://spring.io/projects/spring-boot) pour le back-end.

## Démarrer le projet

### Clonage du projet

git clone https://github.com/TheoKennel/Testez-une-application-full-stack.git

## Back-end

### Aller dans le dossier

cd back

### Démarrer les tests unitaires

mvn clean test

### Démarrer les tests d'intégration + tests unitaires

mvn clean verify

## Front-end

### Aller dans le dossier

cd front

### Installer les dépendances

npm install

### Lancer le front-end

npm run start;


### Lancer les tests e2e

npm run e2e

### Générer le rapport de couverture (vous devez lancer les tests e2e avant)

npm run e2e:coverage

## Tests unitaires

### Lancer les tests

npm run test

### Suivre les modifications

npm run test:watch 


## Compte administrateur par défaut

- **Login** : yoga@studio.com
- **Mot de passe** : test!1234

## Rapport

Le rapport est disponible ici : `front/coverage/lcov-report/index.html`
