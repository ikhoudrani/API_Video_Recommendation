Pour les tests :

Se mettre dans le bon répertoire ( videoReference )

`mvn test -Dtest=VideoServiceTest`
`mvn test -Dtest=Step{number}Test`

Avec number étant 1, 2, 3 ou 7.
Le test pour l'étape 5 étant fait dans celui de l'étape 1.

Pour chacune des étapes suivantes, lancer d'abord l'application en se mettant à la racine du projet et en lançant :
`mvn spring-boot:run`

# ETAPE 1

```shell
curl -X POST http://localhost:8080/videos -H "Content-Type: application/json" -d '{"id": "1", "title": "Breaking Bad","labels": ["adventure", "whip", "archeology"],"numberOfEpisodes" : "62"}'
```
# ETAPE 2

```shell
curl http://localhost:8080/videos/1
```

ou depuis le navigateur web en allant sur :
http://localhost:8080/videos/1

# ETAPE 3 

Ajouter plusieurs films :

```shell
curl -X POST http://localhost:8080/videos -H "Content-Type: application/json" -d '{"id": "2", "title": "Les Indestructibles","labels": ["family", "action"],"director": "Brad Bird", "releaseDate": "2004-11-05T12:00:00Z"}'
```

```shell
curl -X POST http://localhost:8080/videos -H "Content-Type: application/json" -d '{"id": "3", "title": "Indiana Jones","labels": ["sci-fi", "dystopia", "action"],"director": "Brad Bird", "releaseDate": "1982-03-18T12:00:00Z"}'
```

```shell
curl -X POST http://localhost:8080/videos -H "Content-Type: application/json" -d '{"id": "4", "title": "Matrix","labels": ["sci-fi", "action"],"director": "Lana Wachowski", "releaseDate": "1999-03-31T12:00:00Z"}'
```

http://localhost:8080/videos/search?title=ind

# ETAPE 5

Ajoutez un film et une série ou utiliser celles utilisées au préalable.

Rappel :

Ajout d'un film :
```shell
curl -X POST http://localhost:8080/videos -H "Content-Type: application/json" -d '{"id": "4", "title": "Matrix","labels": ["sci-fi", "action"],"director": "Lana Wachowski", "releaseDate": "1999-03-31T12:00:00Z"}'
```

Ajout d'une série :

```shell
curl -X POST http://localhost:8080/videos -H "Content-Type: application/json" -d '{"id": "1", "title": "Breaking Bad","labels": ["adventure", "whip", "archeology"],"numberOfEpisodes" : "62"}'
```

Vérification :
`curl http://localhost:8080/videos/movies`
`curl http://localhost:8080/videos/series`

Ou directement depuis le navigateur web :
http://localhost:8080/videos/movies
http://localhost:8080/videos/series


# ETAPE 7

Utiliser les films ajoutés à l'étape 3

Tester dans le navigateur avec :

http://localhost:8080/videos/3/similar?minLabels=1

Puis :

http://localhost:8080/videos/3/similar?minLabels=2
