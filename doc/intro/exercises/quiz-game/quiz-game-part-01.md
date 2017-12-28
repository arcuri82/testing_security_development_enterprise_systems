# Quiz Game - Part 01

Create a new Maven project for a Quiz Game.
Lesson after lesson, we will add new features to it.
We start from setting up Maven and modelling the application's data.

The Maven project should have the following dependencies:

* `org.hibernate.javax.persistence:hibernate-jpa-2.1-api`
* `org.hibernate:hibernate-core`
* `junit:junit`
* `com.h2database:h2`

Note: the dependencies above are specified with the format `groupId:artifactId`. 
When adding a dependency, not only you need to also specify a `version`,
but also the appropriate `scope`.

Configure the `maven-compiler-plugin` to use a specific version
of Java (e.g., Java 1.8).

Create 3 entity classes:

* *Category*: with a numeric id, and a String name
* *SubCategory*: with a numeric id, and a String name
* *Quiz*: with a numeric id, a String question, 4 String answers,
          and a numeric index representing the correct answer
          
Add a `peristence.xml` configuration, enabling Hibernate and H2 with
locally handled transactions.

Write a `QuizEntityTest` class, where you should have a test 
called `testQuiz` where you create a `Quiz` object, and persist it
into the H2 database. Verify that the quiz can be saved into the
database.


Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-01` module.            