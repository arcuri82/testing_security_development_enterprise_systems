# Quiz Game - Part 01

Create a new Maven project for a Quiz Game.
At this point,
it should contain only one module (i.e., only one `pom.xml` file).
Lesson after lesson, we will add new features to it.
We start from setting up Maven and modelling the application's data.

The Maven project should have the following dependencies:

* `org.hibernate:hibernate-core`
* `org.junit.jupiter:junit-jupiter-engine`
* `com.h2database:h2`
* `javax.xml.bind:jaxb-api`
* `org.glassfish.jaxb:jaxb-runtime`

Note: the dependencies above are specified with the format `groupId:artifactId`. 
When adding a dependency, not only you need to also specify a `version`,
but also the appropriate `scope`.
To avoid possible conflicts in the dependency versions,
use the same ones used in this repository.
  

Configure the `maven-compiler-plugin` to use a specific version
of Java (i.e., Java 11).

Create 3 entity classes:

* *Category*: with a numeric id, and a String name
* *SubCategory*: with a numeric id, and a String name
* *Quiz*: with a numeric id, a String question, 4 String answers,
          and a numeric index representing the correct answer
          
Add a `persistence.xml` configuration, enabling Hibernate and H2 with
locally handled transactions.

Write a `QuizEntityTest` class, where you should have a test 
called `testQuiz` where you create a `Quiz` object, and persist it
into the H2 database. Verify that the quiz can be saved into the
database.


Solutions to this exercise can be found in the 
`intro/exercise-solutions/quiz-game/part-01` module.

Note: the solution module `quiz-game-part01` is **NOT** self-contained, as it does
have a `<parent>` tag, and a reference to `jee-provided-dependencies`.
When you create your own Maven project, you should not use any `<parent>` tag, nor
use any SNAPSHOT dependency from this repository.
You can of course use dependencies from Maven Central. 
      