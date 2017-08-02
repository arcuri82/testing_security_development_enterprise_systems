[![Build Status](https://travis-ci.org/arcuri82/testing_security_development_enterprise_systems.svg?branch=master)](https://travis-ci.org/arcuri82/testing_security_development_enterprise_systems)

# Testing, Security and Development of Enterprise Systems

This repository contains a set of examples related to the testing, security
and development of enterprise systems.
Currently, this repository focuses on Java (version 8), and JEE (version 7) 
in particular.

This repository is still in an early stage of development (e.g., there are plans to
add more on Spring, Kotlin and NodeJS).
Documentation (early work in progress) is available 
[here](doc/main.md).

### Requirements

Code needs to be built with Maven 3.x.
Most tasks are automated via Maven.
For example, JEE code that needs to run in a container will automatically download
and install Wildfly.
The only exception is the need to manually download Chrome and Chrome Driver 
(this latter needs to be put under your home folder).
This is needed to run the Selenium tests.
The code should run on all major operating systems, i.e. Mac, Linux and Windows.


### Useful Maven Commands

* `mvn install -DskipTests`

  this will compile all the code and install all the generated jar files into 
  your local Maven repository. Does not run the tests.
   
   
* `mvn verify -P selenium`
   
   compile, package the code and run all the unit and integration tests. 
   The Selenium tests are not run by default, and you need to use `-P selenium`
   to activate the Maven profile to run them.
   
* `mvn wildfly:run -DskipTests`
   
   from the modules in which WAR files are generated for JEE, you can start
   Widlfly with the given packaged WAR.


### Running The Tests

Besides Maven, you can run tests directly from an IDE (e.g., IntelliJ or Eclipse).
There are two exceptions though:
 
1. Arquillian tests need Wildfly installed. This is automatically done via 
   Maven by executing for example `mvn test`. This needs to be done only once, 
   as Wildfly gets installed
   under the `target` folder of the module.
   Note: if you do a `mvn clean`, then you will need to do a `mvn test` again to
   re-deploy Wildfly.
   
2. Selenium tests need Wildfly as well, but they do not start the container by 
   themselves. If you want to run such tests from an IDE, 
   you need first to manually start Wildfly from command line with for example
   `mvn wildfly:run`.   



### ![](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of 
<a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a>
and 
<a href="https://www.yourkit.com/.net/profiler/">YourKit .NET Profiler</a>,
innovative and intelligent tools for profiling Java and .NET applications.