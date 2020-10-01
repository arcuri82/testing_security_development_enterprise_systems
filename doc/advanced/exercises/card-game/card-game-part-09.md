# Card Game - Part 09

## Auth API

Create a new `auth` API, with endpoints to create new users, login and logout.
Authentication should be cookie-session based, with sessions stored in Redis,
using Spring Security.
Passwords should be hashed with BCrypt, stored in a Postgres database.
Write integration tests with RestAssured, where Redis is started with TestContainers. 
Most of the code in this API will be based on what shown in class. 

Update the routes in `api-gateway` to handle this new `auth` API.  

## User-Collections

The `user-collections` API is the only one that will need security.
First you will need to add the following dependencies:

* `org.springframework.boot:spring-boot-starter-security`
* `org.springframework.cloud:spring-cloud-starter-security`
* `org.springframework.session:spring-session-data-redis`
* `org.springframework.boot:spring-boot-starter-data-redis`


You need to add `WebSecurityConfig`, based on sessions stored in Redis.
This will need to be configured in `application.yml`, e.g.:
```
redis:
      host: redis
      port: 6379
session:
      store-type: redis
``` 

For access control, users should be able to access their own collections, but not
the ones of the other users (should return a proper 403 in those cases).

Once the security is in place, you will need to update most of the test cases.
However, to test this API in isolation (without having both `auth` and Redis up and running), 
you will need to change the auth done in the tests,
e.g., using `httpBasic`. For this, you will need to create a `WebSecurityConfigLocalFake`,
as seen in class.
Make sure to have the following in the properties to make it work:
`spring.main.allow-bean-definition-overriding=true`.
In the test, might want to disable sessions and Redis.


## Docker-Compose

In `docker-compose.yml`, add a service for `auth`, a new Postgres instance for it,
and Redis. 


## E2E-Tests

Update all failing E2E-tests that require auth. 
Add new tests to explicitly check the access control in `user-collections`, i.e.,
check for both cases of 401 (not authenticated) and 403 (authenticated but forbidden).


Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-09` module.

