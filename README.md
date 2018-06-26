# Testing, Security and Development of Enterprise Systems

![](doc/img/glenn-carstens-peters-120205.jpg  "Photo by Glenn Carstens-Peters on Unsplash")


<!--- Travis CI build status banner -->
[![Build Status](https://travis-ci.org/arcuri82/testing_security_development_enterprise_systems.svg?branch=master)](https://travis-ci.org/arcuri82/testing_security_development_enterprise_systems)


This repository contains a set of examples related to the testing, security
and development of enterprise systems.
Currently, this repository focuses on Java/Kotlin, 
targeting frameworks like Spring and Java EE.

The material in this repository is used in two university-level courses at
the university college [Høyskolen Kristiania](https://kristiania.no/).
In particular:

* *PG5100 Enterpriseprogrammering 1*: introduction to enterprise programming. 
   Documentation [here](doc/intro/main.md).

* *PG6100 Enterpriseprogrammering 2*: advanced enterprise programming.
   Documentation [here](doc/advanced/main.md). 

 
 

The repository is built with Maven, and it is divided in two main sub-modules:

* `intro`: material used in the first PG5100 course, where the goal is to be able to build
           a web application accessing a SQL database, and deployed on a cloud provider.
           Main technologies: Java, Java EE, JPA, EJB, JSF, Wildfly, SpringBoot, Spring Security, 
           Selenium, Docker.
           
* `advanced`: material used in the second PG6100 course, where the goal is to dig into the details
            of RESTful APIs and microservices.
            Main technologies: Kotlin, SpringBoot, REST, Docker, Spring Security, Spring Cloud, AMQP.            


### Philosophy of This Repository

There are many resources (e.g., courses and books) out there that deal with the
*development* of enterprise/web systems, using different technologies (e.g, Java and C#). 
However, often such resources only deal with the *development* of these systems,
whereas important concepts like *testing* and *security* are treated like 
just secondary concerns, if treated at all.
This situation has been improved in recent years, but more could be done, and we hope that 
the courses in this repository are a step in that direction.

There are plenty of applications out of there that are afflicted with bugs and
security holes. 
Correctness and security should play a major role when developing software,
and we try to reflect it in this repository.

Furthermore, software engineering is a practical discipline, like any other 
engineering discipline. 
As such, although theory is important, it is also important to get your hands 
*dirty* by actually developing software, and putting theory into practice.
Therefore, in this repository, all concepts are explained also via examples,
with test cases (unit and integration/system ones).
In other words, we follow the principle of *Code is King*, i.e., if something
is worth discussing, then you must have a working example with test cases for it.
In the past, it would had been a problem when you had to download and configure
all needed software manually, like for example a PostgreSQL database or a RabbitMQ
server. 
Fortunately, with the coming of *Docker*, this is not a problem any more.   
    
In this repository two languages are used: *Java* and *Kotlin*.
Also two different frameworks are used: *Spring* and *Java EE*.
Why such choices? 
When studying the concepts of enterprise software development/testing/security,
the actual used languages/frameworks are not so important.
For example, using C# with .Net would had been a viable option as well.
The languages/frameworks are just used to get *practice*, and get your hands dirty.
When you get a degree in software engineering, by all means afterwards
you could end up working with C#/.Net and never touch Java again. 
Therefore, it is important to learn the fundamental concepts behind those 
languages/frameworks, and not just their low level technical details. 

Trying all possible languages and frameworks would be great. 
However, when studying 
such topics for a university degree, time is limited, and one needs to make
some choices.
And switching between too many languages/frameworks would just be a too large overhead
(e.g., learning of different IDEs and building tools).
The motivation for choices of language/frameworks in this repository is as
follows:

* `Java`: one of the most used programming languages, with a very large
  ecosystem of existing applications and libraries.
  Java is one of the main languages for enterprise development 
  (if not *the* main language). 
  Enterprise systems are often large and complex, and so a *statically typed*
  language is recommended. 
  In our personal opinion, this excludes languages like JavaScript, Python, Ruby, etc.
  TypeScript is statically typed, but it is still JavaScript in its core...  
  Nowadays, C# is a good option, and as a language might even be considered
  better than Java.
  However, it does not have as large ecosystem as Java.  

* `Kotlin`: our language of choice. It is a better Java that can reuse all
    of its existing ecosystem. 
    However, it does have more abstractions and "magic" than Java, which arguably
    means that it is not suited to learn as first language, i.e., better
    to learn Java first.
    That is the reason why it is only used in the `advanced` course, and not the
    `intro` one.
    Furthermore, job-wise, Kotlin is not so popular yet.


* `Spring`: our framework of choice. SpringBoot is simply great.
    It does have a non-trivial learning curve though.
    However, once you understand the concepts of dependency injection and
    proxy classes, it is a great tool.
    For the development of web services, DropWizard can be a good choice
    as well, especially if you do not like the "magic" of Spring and want
    a more direct/explicit library. 
    
    
* `Java EE`: in theory the "official" Java framework for enterprise development.
   However, in our opinion, it is much worse than Spring.
   Job-wise, at least in Norway, it is used less and less. 
   Still, it is important to look at different frameworks. 
   As the jump from Java EE to Spring is relatively simple, it is a good
   choice as starting point before moving into SpringBoot.
   Furthermore, you cannot really appreciate SpringBoot until you have
   gone through the blood, sweat and tears of debugging an
   EJB test using Arquillian to deploy to a Wildfly container. 


### Documentation

This repository is still in an early stage of development.
Documentation (early work in progress) is available 
[here](doc/intro/main.md) for the `intro` course, and
[here](doc/advanced/main.md) for the second `advanced` course 
(this latter has code examples, but no documentation yet).

### Requirements

* JDK 1.8 or higher

* An IDE (recommended IntelliJ IDEA Ultimate Edition)

* Maven 3.x

* Docker 

* Chrome and Chrome Driver (only needed to run Selenium tests locally instead of in Docker)

The code in this repository should run on all major operating systems, i.e. Mac, Linux and Windows.

On Windows, if you have problems with too long file names 
when checking out the code with Git, then you might need to run
the following command on a terminal:

`git config --system core.longpaths true`




### Useful Maven Command

* `mvn clean install -DskipTests`

  this will compile all the code and install all the generated jar files into 
  your local Maven repository. Does not run the tests.
  **Note**: first time your run it, it might take a long while, as needing to download
  many dependencies.
   
 

### How to Contribute

There are many ways in which you can contribute. 
If you found the material in this repository of any use, the easiest
way to show appreciation is to *star* it.
Furthermore, if you find issues, you can report them on 
the [issues](https://github.com/arcuri82/testing_security_development_enterprise_systems/issues) 
page.
Possible types of issues:
  
* Some of the code examples are unclear, or with not enough
  documentation to understand exactly what is going on.
   
  
* You find a *non-intended* security vulnerability or bad practice in any of the 
  code examples.
  Note: in some cases, for didactic reasons there will be non-secure code.
  But in those cases that should be explicitly stated.

* Comments regarding a tool/library/framework are no longer valid (e.g., since a new version
  has been released).

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