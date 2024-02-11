# Projet Yoga-App
## Features

- Authentification with login and register (as user or admin)
- Visualize sessions list
- Create, edite and delete session

## Front-end

Le front-end de l'application est développé en Angular.

### I. Installation
Avant de pouvoir exécuter les tests du front-end, installer toutes les dépendances nécessaires :

```sh
npm install
```

### II. Exécution des tests
1. Pour exécuter les tests end-to-end du front-end :

```sh
npm run e2e
```

2. Pour exécuter les tests Jest du front-end :

```sh
npm run test
```

### III. Rapport de couverture :

> End-to-end :
```sh
npm run e2e:coverage
```
- Available here : <b>front/coverage/lcov-report/index.html</b>

> Jest :
```sh
node_modules/.bin/jest --coverage
```

## Back-end

Le back-end de l'application est développé en Java Spring.

Installer la base de données en exécutant le script SQL se trouvant dans le dossier ressources.

```sh
mvn clean test
```

- Available here : <b>back/target/site/index.html</b>


