# Starting the application

1. Start the mysql docker container in `/docker` with `docker-compose up`
2. Start the api with `gradlew bootRun`
3. Optionally run nl.quintor.recipes.DatabasePopulator for some test recipes

# Endpoints
## LIST
GET http://localhost:8080/recipes

## LIST With Filter
Filter structure 
```
filters={
  "exclusions": [
    {
      "property": "ingredients",
      "value": "Salmon",
      "exact": true
    }
  ],
  "inclusions": [
    {
      "property": "instructions",
      "value": "oven",
      "exact": false
    }
  ]
}
```

### All vegetarian
GET http://localhost:8080/recipes?filters=%7B%22exclusions%22%3A%5B%5D%2C%22inclusions%22%3A%5B%7B%22property%22%3A%22categories%22%2C%22value%22%3A%22Vegetarian%22%2C%22exact%22%3Atrue%7D%5D%7D

### Can Serve 4 and have potatoes
GET http://localhost:8080/recipes?filters=%7B%22exclusions%22%3A%5B%5D%2C%22inclusions%22%3A%5B%7B%22property%22%3A%22servings%22%2C%22value%22%3A%224%22%2C%22exact%22%3Atrue%7D%2C%7B%22property%22%3A%22ingredients%22%2C%22value%22%3A%22Potato%22%2C%22exact%22%3Atrue%7D%5D%7D

### Recipes without salmon that have oven in instructions
GET http://localhost:8080/recipes?filters=%7B%22exclusions%22%3A%5B%7B%22property%22%3A%22ingredients%22%2C%22value%22%3A%22Salmon%22%2C%22exact%22%3Atrue%7D%5D%2C%22inclusions%22%3A%5B%7B%22property%22%3A%22instructions%22%2C%22value%22%3A%22oven%22%2C%22exact%22%3Afalse%7D%5D%7D


## GET ONE
GET http://localhost:8080/recipes/1

## DELETE ONE
DELETE http://localhost:8080/recipes/1

## ADD ONE
POST http://localhost:8080/recipes   
BODY EXAMPLE:
```
{
  "categories" : [ "Vegetarian", "Vegan" ],
  "ingredients" : [ "Egg" ],
  "servings" : 4,
  "instructions" : "Boil for an hour",
  "name" : "Eggs!"
}
```

## UPDATE ONE
POST http://localhost:8080/recipes/1   
BODY EXAMPLE:
```
{
  "categories" : [ "Vegetarian", "Vegan" ],
  "ingredients" : [ "Egg" ],
  "servings" : 4,
  "instructions" : "Boil for an hour",
  "name" : "Eggs!"
}
```

# Architecture
A layered architecture was used with the following layers
## Data
All classes related to fetching / saving data

## Domain
All classes related to business logic

## Presentation
All user facing classes and rest controllers