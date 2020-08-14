# Card Game - Part 04

In the REST API of `user-collections`, update each endpoint
to use wrapped responses, and customized error handling (as seen in class).
Every time there is a user error, return a meaningful error message in the 
HTTP response. 

To use wrapped responses and custom error handling based on them, 
you need to rely on the classes from the modules:
* `org.tsdes.advanced.rest:rest-dto`
* `org.tsdes.advanced.rest:rest-exception`

However, do **NOT** import those libraries (as they are SNAPSHOTs
that are not published on Maven Central, and so the build
will fail on a different machine), but rather
just copy&paste their code in your project, in your package
hierarchy.  
Note: in the solutions we import those libraries, just for simplicity.
Furthermore, we use:
`@SpringBootApplication(scanBasePackages = ["org.tsdes.advanced"])`
to tell Spring to scan for those beans.
This is unnecessary if they are under your package hierarchy.

Make also sure to have the following in your `application.yml`:
```
spring:
  mvc:
    throwExceptionIfNoHandlerFound: true
  resources:
    add-mappings: false
```

Create a new module `scores` for a REST API that keeps track of the scores
of all players (based on victories and defeats in the game), and provides
a leaderboard.

This new API should have the same structures and features of `user-collections`, e.g.:

* A self-executable uber-jar file.
* Postgres database with Flyway, and embedded H2 for the tests.
* Wrapped responses with custom error handling.
* Documentation with SpringFox.
* Distinction between DTOs and JPA entities. 
* Test cases, both with RestAssured and directly on the `@Service` classes,
  achieving at least 70% code coverage in total.
  
In the database, for each user you need to save data for:
userId, victories, defeats, draws and score. 

The REST API needs the following endpoints:

* `PUT /api/scores/{userId}`, create default stats info (no victories, no defeats) for the given user.  
* `GET /api/scores/{userId}`, get the stats info for the given user.  
* `GET /api/scores`, get the stats of all players. This must be handled with keyset-pagination,
    sorted based on the `score` field (top to bottom).
    
Inside `src/main/kotlin`, create a `FakeDataService` that generates 
*n* player info at random (e.g., *n=50*).
This bean should be activated based only with a profile (using `@Profile`).


Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-04` module. 

      

  






