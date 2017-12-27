# Quiz Game - Part 07


Continue to work on the Maven project started as exercise 
in the first lesson.


Make sure you have installed/configured Docker and VirtualBox
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


Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-07` module. 

Note: to avoid copy&paste of the same code, the solution for part-07 does
use the code of part-06 as a library.

