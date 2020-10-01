# Card Game - Part 06


In the `user-collections` API, add dependency to a circuit break library,
like for example `spring-cloud-starter-circuitbreaker-resilience4j`.
Also add a dependency to `com.github.tomakehurst:wiremock-standalone`.



You need to implement the method `fetchData` in `CardService`.
This needs to communicate with the `cards` API, doing a GET request
on the current version of the cards. 
Use a `RestTemplate` to make such call.

At this point, the IP:port of `card` API is not known yet when making the HTTP GET call.
In `CardService`, elicit such information in a string variable, which is going to
be injected with `@Value` from `application.yml`.
You can use something like `cardServiceAddress: "cards-service:8080"`.
Note: for now your OS will fail to resolve this `cards-service` via DNS (we will see how we
will make it work with Docker in a couple of lessons...).

The HTTP GET call made by `user-collections` towards `cards` needs to be done inside
of circuit breaker.


In `RestAPITest`, we are going to get rid of `FakeCardService`, as
we are going to test the actual implementation of `fetchData`.
To do this, before the tests are run, you need to start a `WireMock` server,
and stop it after all tests are finished.
Make sure that such server is binding to an ephemeral port.
This server will stub `cards`, and return the same fake data as before 
when queried for the correct endpoint in `cards`.
This means you should NOT need to change any of the existing tests in `RestAPITest`.
Note: to make `user-collections` to speak with `WireMock` instead of `cards-service:8080`,
you will need to use an `ApplicationContextInitializer` to change the value
of `cardServiceAddress` after SpringBoot and WireMock have started, but before the
tests are run (and recall that WireMock should run on an ephemeral port).
You will need to activate it with a `@ContextConfiguration`.

Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-06` module.
  
 
