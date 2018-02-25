# Quiz Game - Part 10


In this exercise, the main focus is to add Spring Security to handle the
registration of users.
Only a logged in user should be able to play.
Furthermore, for each user we want to keep track (and display) how many
matches s/he has won/lost.

Note: regarding the security mechanisms, you should re-use (i.e., copy&paste
and then adapt) code from the security module.


To the `pom.xml` file, add this dependency:

* `org.springframework.boot:spring-boot-starter-security`


Create two new entities:

* `User`: used to stored info of the user needed by Spring Security,
  like `username`, `password`, `roles` and `enabled`.
  

* `MatchStats`: containing a reference to a `User`, and the number
  of victories and defeats.


Create two new services for the two new entities:

* `UserService`: have a method to create a new user with a given password. 

* `MatchStatsService`: have methods to report a new victory for a given
  user, to report a new defeat, and to get the current `MatchStats` 
  associated with the user. 



In the `DefaultDataInitializerService` class, besides creating quizzes,
also do create a default user (e.g., username `foo` and password `123`).
This will be useful for when you debug the application: you will not
need each time you start it to register yet again a new user.


Create these new pages:

* `login.xhtml`: provide a form with button in which one can try to login
  by providing a username and a password.
  If one fails to log in, an error messag should be displayed.

* `signup.xhtml`: similar to `login.xhtml`, but instead of logging in an
  existing user, do a create a new one.
  If one fails to sign up, an error message should be displayed.

Update the following existing pages:

* `index.xhtml`: if a user is not logged in, show a message like
  `Log in to play!!!`. Otherwise, show the number of current victories/defeats,
  and also the button to start a new match.
  
* `layout.xhtml`: if a user is not logged in, in the template all pages should
  show links to the login/signup pages.
  Otherwise, show a logout button and also a welcome message that includes
  the username of the logged in user.


Create two new JSF controllers:

* `UserInfoController`: have methods to return the username of the currently
  logged in user, and also a method to retrieve his/her `MatchStats` entry.

* `SignUpController`: provide a method to sign up a new user.
  If the creation is successful, do redirect to the home page.
  Furthermore, a successful creation should also automatically log 
  in the user (besides just saving the user info in the database). 
  In other words, if one creates a new user, then s/he should not need
  to also do a login for that user. 


Update the `MatchController` to handle these cases:

* If a user tries to start a new match while one is going on (because
  s/he does not know the answer for the current quiz), then
  the previous match should automatically result in a defeat.
  However, notice that, by storing the info of an ongoing match in a session bean
  and not the database, a user can still "cheat" by logging out...

* Every time a user wins or loses a match, the `MatchStats` needs to be
  updated.
  
  
Add a `@Configuration` file called `PasswordConfig` in which you
create a `@Bean` for type `PasswordEncoder` with name `passwordEncoder`,
in which you use as concrete instance `BCryptPasswordEncoder`.
In other words, setup BCrypt as the default password encoder for
your application.   
  

Add a `@Configuration` file called `WebSecurityConfig` in which
you set up Spring Security, where user info is stored on database.
An anonymous user should be able to access the home page and the
sign-up/login pages.
However, s/he should not be able to access the `match` and the 
`result` pages.
Have also proper redirection.
For example, if one does log out (which can be done on any page), do
a redirect to the home page.



In the service tests, you need to update the `ResetService` to handle
the new entities introduced in this exercise.

Add a new service test class called `MatchStatsServiceTest` with:

* `testDefaultStats`: test that by default a new user starts with 
  0 victories and 0 defeats.
  
* `testStats`:  create a new user and register the fact that s/he 
  wins 2 matches and loses 1.
  Read back the data from the database, and verify that indeed the database
  contains 2 victories and 1 defeat for that user.


Create a new abstract `LayoutPO` Page Object.
All pages that use `layout.xhtml` as layout will need to extend
such abstract class.
In such Page Object add methods to interact with the layout template
components, like for example the button to logout.  


Create a new `SignUpPO` Page Object for the the `signup.xhtml` page. 

In the `SeleniumLocalIT` test class, add the following test:

* `testCreateAndLogoutUser`: from home page, verify that by default
  a user is not logged in (e.g., there should be no logout button,
  and there should be the links to login/sign-up).
  Go to the sign-up page, and register a new valid user.
  You should be automatically redirected back to the home page.
  Verify that you are actually logged in, and that the username of the
  user is displayed on the page.
  Do press the logout button.
  Verify the user is no longer logged in, and his/her username is no
  longer displayed. 


Note: all previous existing Selenium tests will fail once you are 
adding Spring Security.
The reason is that they try to play the game without having a user
being logged in.
On each of such tests, do first the step to sign-up a new user.
This should be enough to make the tests now passing.


Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-10` module.

Note: to avoid copy&paste of the same code, the solution for `part-10` does
use the code of `test-utils` as library. 