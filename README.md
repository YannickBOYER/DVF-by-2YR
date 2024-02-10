# DVF-by-2YR

Ce projet a pour objectif de permettre à l'utilisateur d'obtenir les différentes transactions immobilières qui ont eu lieu dans une zone choisie. \
Pour cela, nous récupérons un [CSV](https://files.data.gouv.fr/geo-dvf/latest/csv/2023/) contenant ces informations. \
Nous mettons ensuite à disposition une page Web permettant de choisir une zone. \
Le backend avec l'API sont développés en Java. \
Le PDF retourné par l'API sera affiché sur l'écran de l'utilisateur et automatiquement téléchargé. \
Ce PDF sera aussi enregistré sur un serveur de fichiers afin de pouvoir être récupéré plus tard si besoin. \

## Architecture

Le frontend se trouve dans le dossier DFV-front du projet. \
Le backend se trouve dans le dossier DVF du projet. \
Les documents se trouvent dans le dossier DVF\doc du projet. \

## Installation

#### Prérequis
- Docker
- Connexion à Internet 

#### Démarrage
Afin de lancer le projet, merci de suivre ces instructions:
```bash
DVF-by-2YR> docker-compose up
```
Cette commande va démarrer le frontend, le backend et le serveur de fichiers.

### Quelques informations

Backend : http://localhost:8080 \
Frontend : http://localhost:4173 \
Fileserver (MinIO) : http://localhost:9001 (login : root, password : rootroot)

## Tests

Une classe de test est disponible afin de vérifier le bon fonctionnement du backend. \
Pour lancer ces tests, merci d'utiliser l'IDE de votre choix permettant de le faire.
