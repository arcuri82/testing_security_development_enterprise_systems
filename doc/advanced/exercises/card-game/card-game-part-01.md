# Card Game - Part 01

Create a new Maven project for a Collectible Card Game.
It has to be in a different folder outside this repository. 
Lesson after lesson, we will add new features to it.


Start from a root `pom.xml` file, having 2 sub-modules:
* `cards-dto`
* `user-collections`

The `user-collections` will contain a web service, which will depend on
another one called `cards`.
This latter will be implemented at a later stage.
However, to be able to build `user-collections`, we will need to refer to the
DTOs of `cards`, which will be in its own module `cards-dto`.

For both sub-modules, you need to setup the Kotlin compiler and dependencies. 

At this point, `cards-dto` does not need any further dependencies besides Kotlin.
On the other hand, not only `user-collections` needs to import `cards-dto` as a
dependency, but also it needs the following:

* `org.springframework.boot:spring-boot-starter-data-jpa`
* `org.springframework.boot:spring-boot-starter-validation`
* `org.postgresql:postgresql`
* `org.flywaydb:flyway-core`
* `javax.xml.bind:jaxb-api`
* `org.glassfish.jaxb:jaxb-runtime`
* `org.junit.jupiter:junit-jupiter`
* `org.springframework.boot:spring-boot-starter-test`
* `com.h2database:h2`

Note: the dependencies above are specified with the format `groupId:artifactId`. 
When adding a dependency, not only you need to also specify a `version`,
but also the appropriate `scope`.
To avoid possible conflicts in the dependency versions,
use the same ones used in this repository.


The `cards-dto` module will need the following classes:
* `Rarity`: an enum representing the rarity of a card (e.g., BRONZE, SILVER and GOLD).
* `CardDto`: info on a specif card, e.g., id, name, description, rarity and imageId. 
* `CollctionDto`: a full list of `CardDto` for each card in the game, plus info on
   card costs, selling/milling values, and probability to find a card when opening a pack.
   Those should be maps depending on the rarity of the card (i.e., the cost/sell/probability
   of a card only depends on its rarity).   

For the `user-collections` module, we will start with the data layer and business
logic. The REST API will be implemented at a later stage.
Implement the following packages/classes:
* `Application`, entry point for the Spring Boot application.
* `db/CardCopy`, JPA entity representing how many copies of a card a user has.
* `db/User`, JPA entity for users, with info like id, coins (game currency), number of
    owned card packs (still to open), and list of `CardCopy` for the cards s/he owns.
* `db/UserService`, service to operate on the database, with methods for:
    * create a new user, with some initial coins and card packs to open
    * let a user buy a card
    * let a user mill/sell a card
    * let a user open a pack (can assume all packs contain *n* random cards, with for example *n=5*)
* `CardService`, used to *fetch* data from the `Card` REST API (which still needs to be implemented),
    which has the info of all cards in the game.
    For now, you can have a `protected fun fetchData()` that is left empty.
    When the `CollectionDto` is fetched, it should be converted into an internal representation, i.e.,
    the classes `Card` and `Collection`.
    When a `Collection` is instatiated based on an input `CollectionDto`, it must be
    validated, e.g., check that there are info on prices for all possible card rarities.
    This service also need to have a `fun getRandomSelection(n: Int) : List<Card>` method,
    which is going to be used when opening a card pack.
*  `model/Collection`, representing the data of a `CollectionDto`, with input validation.
*  `model/Card`, representing the data of a `CardDto`. However, for the `user-collections`
    API, we just need to keep the data for card id and its rarity.    
         
 
When designing the entities and service operations on the databases, make sure to use
the appropriate constraints and synchronization mechanisms (e.g., pessimistic locks when
doing operations like buying/selling cards). 

You must configure `Flyway`, with Hibernate using `ddl-auto: validate`.
The application must be configured to use Postgres.
However, for the test cases, you should use H2.


Write a `UserServiceTest` integration test, which will run on a H2 database.
Write enough test cases to get a 70% code coverage on `UserService`.
One challenge here is that `CardService.fetchData` is not implemented yet.
And, even if it was, it would depend on an external service.
We are going to see a more sophisticated way to handle this situation, later in the course.
For now, under `src/test/kotlin` we are going to have a new service bean 
called `FakeCardService` which extends `CardService`.
It will implement `fetchData` with same fake data.
To tell Spring to use `FakeCardService` instead of `CardService`, you need to 
use the `@Primary` annotation.
To make `FakeCardService` used only in `UserServiceTest`, you should create (with `@Profile`) 
and activate (with `@ActiveProfiles`) a custom profile.   

 
Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-01` module.

Note: the solution module `card-game-part01` is **NOT** self-contained, as it does
have a `<parent>` tag.
This also means that some dependencies (e.g., Kotlin and JUnit) and Maven plugins
(e.g., the Kotlin compiler) are actually specified in some parent pom files (as inherited).

When you create your own Maven project, you **MUST NOT** use any `<parent>` tag in your root pom file, nor
use any SNAPSHOT dependency from this repository.
You can of course use dependencies from Maven Central. 
      