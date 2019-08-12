# Advanced: Enterprise Programming 2 (PG6100)

The course is composed of 12 lessons, each one lasting between 2 and 4 hours.

## Lessons

* **Lesson 01**: Intro, Kotlin, JSON and Web Services
  
  Slides: [[pdf]](slides/lesson_01.pdf), 
          [[pptx]](slides/lesson_01.pptx)
          
  Modules:     
  
  * **advanced/kotlin**
  * **advanced/data-format**
  * **advanced/calling-webservice**            

<br />

* **Lesson 02**: HTTP and RESTful APIs
  
  Slides: [[pdf]](slides/lesson_02.pdf), 
          [[pptx]](slides/lesson_02.pptx)
          
  Modules:     
  
  * **advanced/example-news**
  * **advanced/rest/news-rest**

<br />

* **Lesson 03**: Charsets, PATCH and GUIs
  
  Slides: [[pdf]](slides/lesson_03.pdf), 
          [[pptx]](slides/lesson_03.pptx)
          
  Modules:     
  
  * **advanced/rest/charset**
  * **advanced/rest/patch**
  * **advanced/rest/gui-v1**

<br />


* **Lesson 04**: Wrapped Responses, Errors and Pagination
  
  Slides: [[pdf]](slides/lesson_04.pdf), 
          [[pptx]](slides/lesson_04.pptx)
          
  Modules:     
  
  * **advanced/rest/wrapper**
  * **advanced/rest/rest-dto**
  * **advanced/rest/exception-handling**
  * **advanced/rest/rest-exception**
  * **advanced/rest/pagination**
  * **advanced/rest/gui-v2**
  
<br />


* **Lesson 05**: 3xx Redirection, Conditional Requests and Caching
  
  Slides: [[pdf]](slides/lesson_05.pdf), 
          [[pptx]](slides/lesson_05.pptx)
          
  Modules:     
  
  * **advanced/rest/redirect**
  * **advanced/rest/conditional-get**
  * **advanced/rest/conditional-change**
  * **advanced/rest/cache**

<br />

* **Lesson 06**: Mocking, Circuit Breakers and Test Generation
  
  Slides: [[pdf]](slides/lesson_06.pdf), 
          [[pptx]](slides/lesson_06.pptx)
          
  Modules:     
    
  * **advanced/rest/wiremock**
  * **advanced/rest/circuit-breaker**

<br />


* **Lesson 07**: SOAP and GraphQL
  
  Slides: [[pdf]](slides/lesson_07.pdf), 
          [[pptx]](slides/lesson_07.pptx)
          
  Modules:     
  
  * **advanced/graphql/base**
  * **advanced/graphql/resolver**
  * **advanced/graphql/database**
  * **advanced/graphql/graphql-dto**
  * **advanced/graphql/mutation**
  * **advanced/graphql/news-graphql**

<br />


* **Lesson 08**: MicroService Architectures
  
  Slides: [[pdf]](slides/lesson_08.pdf), 
          [[pptx]](slides/lesson_08.pptx)
          
  Modules:     
  
  * **advanced/advanced/microservice/discovery/***
  * **advanced/advanced/microservice/gateway/***
  
<br />

* **Lesson 09**: Security in MicroServices
  
  Slides: [[pdf]](slides/lesson_09.pdf), 
          [[pptx]](slides/lesson_09.pptx)
          
  Modules:     
  
  * **advanced/advanced/security/basic**
  * **advanced/advanced/security/session**
  * **advanced/advanced/security/distributed-session/***
  
<br />


* **Lesson 10**: To be decided

<br />


* **Lesson 11**: AMQP and RabbitMQ
  
  Slides: [[pdf]](slides/lesson_11.pdf), 
          [[pptx]](slides/lesson_11.pptx)
          
  Modules:     
  
  * **advanced/advanced/amqp/base-queue**
  * **advanced/advanced/amqp/distributed-work**
  * **advanced/advanced/amqp/fanout**
  * **advanced/advanced/amqp/direct-exchange**
  * **advanced/advanced/amqp/topic-exchange**
  * **advanced/advanced/amqp/amqp-rest**


* **Lesson 12**: Revision

<br />

 
## External Resources

* [RFC-5789](https://tools.ietf.org/html/rfc5789): *PATCH Method for HTTP*. 
  All of it.
* [RFC-7230](https://tools.ietf.org/html/rfc7230): *Hypertext Transfer Protocol (HTTP/1.1): Message Syntax and Routing*.
  Pages 1-36 (up to beginning of Section 4.1), 
  41-43 (Section 5 to 5.3.2),
  44-47 (Section 5.4 to 5.7),
  and 50-59 (Section 6 to 6.7).
* [RFC-7231](https://tools.ietf.org/html/rfc7231): *Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content*.
  Pages 1-72 (up to Section 7.4.1).
* [RFC-7232](https://tools.ietf.org/html/rfc7232): *Hypertext Transfer Protocol (HTTP/1.1): Conditional Requests*.
  All of it.  
* [RFC-7234](https://tools.ietf.org/html/rfc7234): *Hypertext Transfer Protocol (HTTP/1.1): Caching*. 
  Pages 1-29 (up to Section 5.4).
* [RFC-7235](https://tools.ietf.org/html/rfc7235): *Hypertext Transfer Protocol (HTTP/1.1): Authentication*. 
  Pages 1-9 (up to Section 4.4),
  and 12-13.
* [RFC-7396](https://tools.ietf.org/html/rfc7396): *JSON Merge Patch*.
  All of it.
* [RFC-7538](https://tools.ietf.org/html/rfc7538): *The Hypertext Transfer Protocol Status Code 308 (Permanent Redirect)*.
  All of it.    
* [RFC-7617](https://tools.ietf.org/html/rfc7617): *The 'Basic' HTTP Authentication Scheme*.
  All of it.    
* Todd Fredrich, *RESTful Service Best Practices*, [available in different formats](https://www.restapitutorial.com/resources.html).
  All  of it.  
* Chris Richardson and Floyd Smith, *Microservices From Design to Deployment*.
  Free ebook, but need registration to download its [PDF](https://www.nginx.com/resources/library/designing-deploying-microservices/).
  Note: in that link it is called *Designing and Deploying Microservices*.  All of it.
* [Spring](https://spring.io)
* [Kotlin](https://kotlinlang.org/docs/kotlin-docs.pdf)
* [GraphQL](https://graphql.org/)
* [RabbitMQ](https://www.rabbitmq.com/)


## Exercises

There is no exercise with solutions in this course (yet).

If you have never used Kotlin before, it is recommended to do some
Kotlin Koans, like [this one](https://kotlinlang.org/docs/tutorials/koans.html). 

Before the exam starts, it makes sense to develop a pet project to get practice.
In other words, choose a topic you like, and build a RESTful API for it.
Each class, when a new concept is introduced, extend your project with what
you learn. 

## Exam

In this course the exam is divided in two parts: a written, theoretical exam,
and a project to do at home.
Two examples of mock exam for the theoretical part can be 
found [here (mock_0)](exams/theory_mock_exam_0.pdf)
and [here (mock_1)](exams/theory_mock_exam_1.pdf).
On the other hand, 
an example of mock exam for the project can be 
found [here](exams/project_mock_exam.pdf) (file not available yet).