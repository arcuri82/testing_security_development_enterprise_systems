# Quiz Game - Part 09

In this exercise, we will add Selenium tests for the web application.
We are going to use local drivers, and not Docker yet.
Therefore, you need to install Chrome and Chrome Driver on your machine.

In the `pom.xml` file, add the following dependency:


* `org.seleniumhq.selenium:selenium-java`

Add a `RedirectForwardHandler` controller to do an automated forward from 
the root path `/` to the default home page `/index.xhtml`. 

For each of the three existing pages (i.e., `index.xhtml`, `match.xhtml` and `result.xhtml`),
write a Page Object (e.g., `IndexPO`, `MatchPO` and `ResultPO`).
You can/should reuse code (e.g., copy&paste) from the `org.tsdes.misc:test-utils` module.


Create a `SeleniumLocalIT` test class with the following Selenium tests:

* *testNewMatch()*: from home page, click the new match button, and verify you can see
                    the category selection.

* *testFirstQuiz()*: from home page, click the new match button. On the match page,
                     select a category. 
                     Verify that a quiz is displayed, and that the categories are no
                     longer displayed.
                                          
* *testWrongAnswer()*: start a new match (click on the new match button and then choose a category).
                       At the first quiz, answer it wrongly.
                       Verify that you can see the "lost" message and not the "win" one.                     

* *testWinAMatch()*: start a new match (click on the new match button and then choose a category).
                     Correctly answer the 5 quizzes in the match.
                     At each answer, do verify that the current displayed quiz counter 
                     increases by 1 (i.e., from 1 to 5).
                     Once match is over, do verify that you can see the "win" message and not 
                     the "lost" one.                        

       
When you write these tests, you will need to take the following into account:

* You might need to change the `xhtml` files by adding for example `id` attributes and 
  `prependId="false"` in the forms.
  
* To avoid dependencies among tests, you will need to reset the cookies (in particular the
  session one) before running each test. You can do that by calling 
  `driver.manage().deleteAllCookies()`.
  
* When a quiz is displayed, it will be chosen at random. Therefore, beforehand you will
  not be able to know which is the correct answer.
  Testing non-deterministic software is challenging.
  However, in this particular case, a reasonable approach is to inject a `QuizService` in 
  your tests, and then query the database directly every time you need to know which is 
  the right answer for the currently displayed quiz.    
                    

To your `pom.xml` file, add the `jacoco` and `failsafe` plugins.
When you execute `mvn verify` from command line, not only all the tests 
should be run (including the Selenium ones), but you should also get a test coverage
report at `target/site/jacoco-it/index.html`. 

Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-09` module. 
Note: to avoid copy&paste of the same code, the solution for `part-09` does
use the code of `part-08` and `test-utils` as libraries.
Furthermore, when you run `mvn verify` on this module, you will only get
coverage results for the class `RedirectForwardHandler`, which is the only one
in the module (as all other classes are in `part-08`, which is treated as an
external library).

