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

1. End-to-end :
```sh
npm run e2e:coverage
```
- Available here : front/coverage/lcov-report/index.html

1. Jest :
```sh
node_modules/.bin/jest --coverage
```

## Back-end

Le back-end de l'application est développé en Java Spring.


