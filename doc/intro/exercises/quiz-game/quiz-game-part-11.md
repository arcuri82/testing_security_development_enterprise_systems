# Quiz Game - Part 11


If you have not your code on GitHub yet, do it.
Create an account on Travis CI.
Add a `.travis.yml` file to your project, and configure Travis
to build your project each time there is a push on GitHub.

Add a `readme.md` file in which you give a high level overview of
the project.
Add an image banner  pointing to the current build status on Travis.


The current module structure should be changed.
Instead of one single module, you should have four Maven modules
in total: a root one with three children:

* `backend`: containing all the `@Entity` and `@Service` classes.
* `frontend`: for JSF controller beans, Spring Security and `.xhtml` templates.
* `report`: for aggregated JaCoCo report, and for the OWASP dependency 
            check report. 


Add `Flyway` for handling database migrations. 
Hibernate should no longer do `create-drop`, but rather `validate`.
Note: instead of writing SQL by hand to map the `@Entity` objects,
you can check what Hibernate was generating in the logs.


Configure the `spring-boot-maven-plugin` plugin to generate a self-executable
JAR file for the application.

 
Move the current `SeleniumLocalIT` tests into an abstract
`SeleniumTestBase`.
The `SeleniumLocalIT` will extend such abstract class.
Create a new `SeleniumDockerIT` class extending `SeleniumTestBase`.
Here, you need to run three processes in three different Docker images:
the Spring application, the Chrome browser, and a Postgres database.
All these images should share the same virtual network.

The Postgres database should be started with a `@ClassRule`, so that
it is started only once for all the tests.
The other two images should be started for each test.
To make sure that the browser starts after the Spring application,
you will need to use a JUnit `RuleChain`. 

Note 1: with this configuration, the Spring application will be restarted
for each test, whereas the database is not.
This means that the `Flyway` migration is executed only once, for the
first test. 

Note 2: these settings are quite tricky, because you will still need in
`SeleniumDockerIT` to be able to use `QuizService` inside the tests.


Run your build and tests from command line using `mvn clean verify`.
Not only the build should be successful, but you should also be
able to find the JaCoCo and OWASP reports in the `report` module
inside the `target` folder.
You should have at least a 80% code coverage.
If not, add more tests.


Create an account on `Heroku`, and create an application with a Postgres
database.
Add the  Heroku Maven plugin to deploy your application JAR file
to Heroku.
Make sure that by default the application is configured to connect to the
Heroku Postgres database.
  
 
Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-11` module.
Note: the Heroku application name must be unique. 
So, you will not be able to re-use the same name used in the solutions.
Note 2: solutions have no Travis file. You can just look at the
root one in  this whole repository.


# Extra

This application can be extended in many different ways.
Here some ideas (but no solution provided):

* Give the ability to create and edit quizzes in the database directly
  from the web application.
  
* Create an `admin` role. For example, only an admin should be able to 
  edit quizzes.   

* Make sure that players cannot cheat. For example, currently a player could
  just log out to avoid answering a quiz s/he does not know.

