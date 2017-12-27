# Quiz Game - Part 05

Continue to work on the Maven project started as exercise 
in the first lesson.

In the `pom.xml` file, add the following dependencies:

* `org.wildfly.arquillian:wildfly-arquillian-container-managed`
* `org.jboss.arquillian.junit:arquillian-junit-container`


Furthermore, configure the `maven-dependency-plugin` to download
and unzip Wildfly into the `target` folder.



In your `persistence.xml` file, add a second `<persistence-unit>`
with transaction type `JTA`.
Add the following property: 

`<property name="wildfly.jpa.default-unit" value="true"/>`

which will make sure this is going to be the default configuration.
The existing tests for the `@Entity` classes will still refer to the
other persistence unit, as their transactions are manually handled.

Under the test resources, add an `arquillian.xml` file configured with 
the Wildfly unzipped in the `target` folder. 


In the `src/test/java` folder, and NOT in the `src/main/java`,
create a `ResetEjb` class with a method that deletes all the 
entities in the database.

Write a test class called `CategoryEjbTest`, configured with Arquillian
using a managed Wildfly.
Inject a reference to `CategoryEjb` and `ResetEjb`.
Add a `@Before` method in which the database is reset.
Write at least the following tests for `CategoryEjb`:

* `testNoCategory`: check that database is empty, i.e. no existing categories.

* `testCreateCategory`: create a category, and verify it was created (i.e., non-null id).

* `testGetCategory`: create a category, get its id, and then retrieve the entity
    with that id. Verify that the names do match.

* `testCreateSubCategory`: create a category and a subcategory for it.
    With the subcategory id, retrieve its newly created entity.
    Verify that names do match, as well as the retrieved subcategory has the
    right parent category.
    
* `testGetAllCategories`: create 3 categories. Create 1 subcategory per category.
    Retrieve all categories without loading the subcategories.
    Verify the retrieved categories are 3.
    Try to access the subcategories from one of the retrieved categories, and
    verify that such operation does throw an exception.
    Do again the retrieval of all categories, but this time do load the subcategory.
    Verify you can access the subcategories.     

* `testCreateTwice`: create a category with same name twice. 
    Verify that the second attempt fails.

Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-05` module.    