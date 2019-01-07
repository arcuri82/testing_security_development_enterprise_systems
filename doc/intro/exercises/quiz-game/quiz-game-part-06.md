# Quiz Game - Part 06

Continue to work on the Maven project started as exercise 
in the first lesson.

Create a stateless EJB called `QuizEjb` with the following methods:

* `public long createQuiz(
               long subCategoryId,
               String question,
               String firstAnswer,
               String secondAnswer,
               String thirdAnswer,
               String fourthAnswer,
               int indexOfCorrectAnswer
       )`
       
   Create a new quiz, and return the id of the newly generated entity.

* `public List<Quiz> getQuizzes()`

   Return all existing quizzes.


* `public Quiz getQuiz(long id)`

    Return the quiz with the given id.

* `public List<Quiz> getRandomQuizzes(int n, long categoryId)`

   Return `n` quizzes, chosen at random, without replacement (i.e., they should 
   be all unique, without duplicates).
   If there exist less than `n` quizzes in the database, throw an exception.
   Note: implementing this method in an efficient way is not trivial, as databases
   usually do not natively support random reads. 


Create a singleton EJB called `DefaultDataInitializerEjb` which is 
initialized at startup.
Such EJB should create at least 2 categories, at least 5 subcategories, and
at least 10 questions.
Each category should have at least 5 questions under it.  
Create meaningful questions and categories, as those will be used as the default
ones in the quiz game.


In the test folder, create a `EjbTestBase` abstract class which is responsible
to init Arquillian and reset the database before each test case.
The test classes will extend this abstract class. 

Write a `QuizEjbTest` test class in which you inject a reference to `QuizEjb`.
Write the following tests:

* `testNoQuiz`: verify that there is no existing quiz. Note: this test would
    fail if you do not reset the database in `EjbTestBase`, as the singleton
    `DefaultDataInitializerEjb` would create at least 10 questions.
    
* `testCreateQuiz`: create one quiz. Retrieve all quizzes, and verify that
    there is only 1. Verify this is the one you just created (e.g., if the
    `question` fields are the same).    

* `testNotEnoughQuizzes`: create 3 quizzes. Try to retrieve 5 quizzes at random.
    Verify that this action throws an exception.    

* `testGetRandomQuizzes`: create 3 quizzes. In a loop repeated 50 times, sample
    2 quizzes at random from the database. 
    At each of the 50 iterations, verify that those 2 quizzes are different.
    After the 50 iterations, verify that each quiz was sampled at least once.     
    Note: it might be that, due to randomness, this test fails. 
    But such should have an extremely low probability.


Write a `DefaultDataInitializerEjbTest` test class that does NOT extend `EjbTestBase`,
as it should not reset the database before the tests are run.
Without injecting a reference to `DefaultDataInitializerEjb`, write a test
in which you verify that in the database there is at least 1 category, 1 subcategory
and 1 quiz.  


Solutions to this exercise can be found in the 
`intro/exercise-solutions/quiz-game/part-06` module. 