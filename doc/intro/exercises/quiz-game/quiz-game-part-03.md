# Quiz Game - Part 03

Continue to work on the Maven project started as exercise 
in the first lesson.

In the `pom.xml` file, add the following dependencies:

* `org.hibernate:hibernate-validator`
* `org.glassfish.web:javax.el`
 

Extend the 3 existing entity classes by adding constraints to them:

* *Category*: name should be unique, non-blank, and with a maximum size.
* *SubCategory*: name should be non-blank, and with a maximum size.
                Link to parent should not be null.
* *Quiz*: the question and the 4 answers cannot be blank, and should have 
          a maximum size. Furthermore, the question must be unique.
          The index of the correct answer must be in range 0 to 3 (inclusive).
          Link to subcategory cannot be null.
          
          
After adding these constraints, you should see that `testQuiz` will fail now,
as it does not set the subcategory.
Modify the assertions in that test to verify that indeed it is not possible
to save such entity.


Create a test class `CategoryEntityTest` with  the following tests:

* `testTooLongName`: create a category with a too long name.
   Verify that it is not possible to persist it into the database.
   Then, verify that, if you use a short name, you can persist it.
   
* `testUniqueName`: create a category and verify you can persist it into
   the database. Create a new category with exactly same name.
   Verify you cannot persist this newly created entity.   

In the test folder, create an *abstract* class named `EntityTestBase`.
Both  `QuizEntityTest` and `CategoryEntityTest` should extend such class.
Move all the shared code (e.g., the `@BeforeEach`/`@AfterEach` database handling methods)
into such abstract class.
Note: it is important that such class name does not end with `Test`, otherwise
it would be executed as test by Maven.

Solutions to this exercise can be found in the 
`intro/exercise-solutions/quiz-game/part-03` module.            