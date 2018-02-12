# Quiz Game - Part 10

TODO

In this exercise, we will add Spring Security 

pom:
spring-boot-starter-security


user in DefaultDataInitializerService


added User and MatchStats entities, and services for them

update MatchController
added UserInfoController, SignInController

added PasswordConfig, WebSecurityConfig


GUI:
added login.xhtml, signup.xhtml
update index.xhtml and layout.xhtml


TODO update Selenium, also in 11


tests:
updated ResetService for new entities
added MatchStatsServiceTest

Selenium:
abstract LayoutPO (all others extend it)
SignUpPO
testCreateAndLogoutUser

Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-10` module.

Note: to avoid copy&paste of the same code, the solution for `part-10` does
use the code of `test-utils` as library. 