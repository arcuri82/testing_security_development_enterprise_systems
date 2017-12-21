[![Build Status](https://travis-ci.org/arcuri82/testing_security_development_enterprise_systems.svg?branch=master)](https://travis-ci.org/arcuri82/testing_security_development_enterprise_systems)

# Testing, Security and Development of Enterprise Systems

![](doc/img/glenn-carstens-peters-120205.jpg  "Photo by Glenn Carstens-Peters on Unsplash")


This repository contains a set of examples related to the testing, security
and development of enterprise systems.
Currently, this repository focuses on Java/Kotlin, 
targeting frameworks like Spring and Java EE.

The material in this repository is used in two university-level courses at
the university college [Westerdals Oslo ACT](https://www.westerdals.no/).
In particular:

* *PG5100 Enterpriseprogrammering 1*: introduction to enterprise programming.

* *PG6100 Enterpriseprogrammering 2*: advanced enterprise programming. 


The repository is built with Maven, and it is divided in two main sub-modules:

* `intro`: material used in the first PG5100 course, where the goal is to be able to build
           a web application accessing a SQL database, and deployed on a cloud provider.
           Main technologies: Java, Java EE, JPA, EJB, JSF, Wildfly, SpringBoot, Spring Security, 
           Selenium, Docker.
           
* `advanced`: material used in the second PG6100 course, where the goal is to dig into the details
            of RESTful APIs and microservices.
            Main technologies: Kotlin, SpringBoot, REST, Docker, Spring Security, Spring Cloud, AMQP.            

### Documentation

This repository is still in an early stage of development.
Documentation (early work in progress) is available 
[here](doc/intro/main.md) for the `intro` course, and
[here](doc/advanced/main.md) for the second `advanced` course.

### Requirements

* JDK 1.8 or higher

* Maven 3.x

* Docker 

* Chrome and Chrome Driver (only needed to run Selenium tests locally instead of in Docker)

The code in this repository should run on all major operating systems, i.e. Mac, Linux and Windows.


### Useful Maven Command

* `mvn clean install -DskipTests`

  this will compile all the code and install all the generated jar files into 
  your local Maven repository. Does not run the tests.
  **Note**: first time your run it, it might take a long while, as needing to download
  many dependencies.
   
 
 <!---   
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
-->

### How to Contribute

There are many ways in which you can contribute:

* If you found the material in this repository of any use, the easiest
  way to show appreciation is to *star* it.
  
* If you find some of the code examples unclear, or with not enough
  documentation to understand exactly what is going on, write 
  about it on 
  the [issues](https://github.com/arcuri82/testing_security_development_enterprise_systems/issues) 
  page.
   
  
* If you find a *non-intended* security vulnerability or bad practice in any of the 
  code examples, please report it on the [issues](https://github.com/arcuri82/testing_security_development_enterprise_systems/issues) 
  page.
  Note: in some cases, for didactic reasons there will be non-secure code.
  But in those cases that should be explicitly stated.


### License & Copyright

The materials herein are all (c) 2017-now [Andrea Arcuri](http://www.arcuriandrea.org) 
and [contributors](https://github.com/arcuri82/testing_security_development_enterprise_systems/graphs/contributors).
The material was/is produced while working at Westerdals Oslo ACT.

All the source code in this repository is released under 
[LGPL version 3 license](LICENSE).

<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">
<img alt="Creative Commons License" style="border-width:0" 
src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" /></a>
<br />
The documentation is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">Creative Commons Attribution-NonCommercial-NoDerivs 4.0 Unported License</a>.




### ![](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of 
<a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a>
and 
<a href="https://www.yourkit.com/.net/profiler/">YourKit .NET Profiler</a>,
innovative and intelligent tools for profiling Java and .NET applications.