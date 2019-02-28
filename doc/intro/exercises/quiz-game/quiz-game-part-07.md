# Quiz Game - Part 07


Continue to work on the Maven project started as exercise 
in the first lesson.


Make sure you have installed/configured Docker 
on your machine.

In IntelliJ, if not already present, 
install the *Docker integration* plugin, which will help
you running Docker directly from the IDE.
If you are using a different IDE than IntelliJ, install any 
similar plugin if available.


In the `pom.xml` file, make sure that the `<packaging>` is set to `war`
and not `jar`.
Also, under `<build>`, add the tag `<finalName>` to specify the name 
`quizgame`.

Add a `web.xml` configured to activate a JSF servlet.


Write a `@SessionScoped` JSF bean called `MatchController`.
A "match" is a sequence of 5 random quizzes from the same chosen
category.
If any of the 5 quizzes is wrongly answered, the match is lost. 
Before starting a match, a user can choose a category (but not subcategory)
among the existing ones in the database. 
 
The `MatchController` class should keep track of the current match (only one
active per session)
and have *at least* the following methods:



* `public boolean isMatchOn()` 

    Check if there is an ongoing match.

* `public String newMatch()` 

    Start a new match.

* `public List<Category> getCategories()`

    Return all available categories.
    

* `public boolean isCategorySelected()` 

    Check if the category for the current match has been selected.

* `public void selectCategory(long id)`

    Select a category with the given id for the current match.  

* `public Quiz getCurrentQuiz()`

    Return the current quiz (out of 5) in the match. 

* `public String answerQuiz(int index)`

    Answer the current quiz with the answer at the given index (e.g., 0 to 3 included).


Create the following JSF pages:

* `index.xhtml`: home page, with button to start a new match in the `match.xhtml` page.

* `match.xhtml`: if there is no ongoing match, provide a button to start new match.
    If a match is already started, but no category is selected yet, display a button for
    each existing category to select them.
    If a category is selected, show the current quiz to answer, with the 4 answers.
    If the user answers correctly, show the next quiz.
    If all of the 5 quizzes are answered correctly, redirect to `result.xhtml`,
    which should show a victory message.
    At each quiz, should display its index, e.g., "2 of 5" for the second quiz.
    If any quiz is wrongly answered,  redirect to `result.xhtml`,
    which should show a match lost message.
    Can use a URL query parameter to distinguish between victory and defeat.

* `result.xhtml`: display either a victory or a match lost message.
    Provide 2 buttons: one to start a new match, and one to go back
    to the home page.
    
All these 3 JSF pages should share the same `layout.xhtml` template.
Add a common footer in this template (actual content is not important).

Add a CSS file shared by all the pages, with some basic settings (e.g.,
background color). The actual content does not really matter. 

Add a Docker file in which you deploy the generate WAR file into a
WildFly container.

When the Docker image is started with exposed port 8080, you should be
able to access the webpage at:

`localhost:8080/quizgame`
     

Solutions to this exercise can be found in the 
`intro/exercise-solutions/quiz-game/part-07` module. 

Note: to avoid copy&paste of the same code, the solution for `part-07` does
use the code of `part-06` as a library.

