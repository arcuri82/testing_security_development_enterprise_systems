# Quiz Game - Part 04

Continue to work on the Maven project started as exercise 
in the first lesson.

In the `pom.xml` file, add the following dependency:

* `javax:javaee-api`


Create a `Stateless` EJB called `CategoryEjb`.
Such class should be in a different folder/package from where you 
chose to have your `@Entity` classes.

Inject a `EntityManager` into the EJB, and then write the following methods:

* `Long createCategory(String name)`
  
  Create and persist a category with a given name. 
  Return the id of newly created entity.
  
* `Long createSubCategory(long parentId, String name)`

  Create and persist a subcategory with a given name and belonging to the 
  specified category.
  Return the id of newly created entity.

* `List<Category> getAllCategories(boolean withSub)`

    Return all the existing categories. The boolean input specifies whether
    the subcategories should be forced-loaded as well inside that method. 

* `Category getCategory(long id, boolean withSub)`

    Return the category specified by id. The boolean input specifies whether
    the subcategories should be forced-loaded as well inside that method. 

    

* `SubCategory getSubCategory(long id)`

   Return the subcategory specified by id.


Note: before you can write tests for this EJB, you need to learn
to use Arquillian.
We will see it in the next lesson.


Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-04` module.     
  