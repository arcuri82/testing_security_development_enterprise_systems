# Quiz Game - Part 05

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


Write a test class called `CategoryEjbTest`, configured with Arquillian
using a managed Wildfly.
Inject a reference to `CategoryEjb`.
Write at least one test for each method in `CategoryEjb`.

Solutions to this exercise can be found in the 
`intro/exercises/quiz-game/part-05` module.    