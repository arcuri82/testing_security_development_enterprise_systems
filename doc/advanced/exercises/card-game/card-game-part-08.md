# Card Game - Part 08

## Gateway

Create a new `api-gateway` service.
It will need to have dependencies for:

* `org.springframework.cloud:spring-cloud-starter-gateway`
* `org.springframework.cloud:spring-cloud-starter-consul-discovery`
* `org.springframework.boot:spring-boot-starter-actuator`

In `application.yml`, make sure that the gateway connects to the
Discovery Service (i.e., `Consul`), and that is forwarding the API requests
to the right service (i.e., `cards`, `scores` and `user-collections`),
using load-balancing (i.e., `lb` instead of `http` in the `uri` entries).
To make this work, make sure that each API has its `spring.application.name`
properly set (as those are the ones used in the `uri` entries).


## REST APIs

In the 3 APIs (`cards`,`scores` and `user-collections`), add the following
dependencies:

* `org.springframework.cloud:spring-cloud-starter-consul-discovery`
* `org.springframework.boot:spring-boot-starter-actuator`

In `application.yml`, make sure to configure the connections to Consul.
However, it should be deactivated in the test configurations, e.g.,
by using `spring.cloud.consul.enabled=false`

In `user-collections`, the HTTP call towards `cards` should be load-balanced.


## Docker

Each application (i.e., the 3 REST APIs and Gateway) should have a 
`Docker` file, running the packaged `jar` files.

## Docker-Compose

Create a `docker-compose.yml`, in which you start the following services:

* `api-gateway`
* `consul`
* `user-collections`
* `postgres` for `user-collections` 
* `scores`
* `postgres` for `scores`
* `cards` (replicated in 2 instances)

Recall that the names for the services you choose in this file are 
very important, as those will resolvable from the Docker-Compose's DNS.
This means that, when in the `application.yml` files you need to define
the URLs for Consul and Postgres instances, you will need to use those names.
The APIs needing a database must use Postgres instead of H2.

You need to open the port for the `api-gateway`, e.g.,
```
ports:
  - "80:8080"
```
This must be the ONLY port open, and reachable from outside Docker-Compose,
i.e., all incoming communications must go through the gateway.


## End-to-End Tests

Create a `e2e-tests` module for End-to-End tests.
Make sure that the `failsafe` plugin is configured/activated, and that
your test'names end with `IT` and not `Test` (so that they will be executed
by `failsafe` instead of `surefire`, after the Maven `package` phase).
Also make sure to add a dependency to all the other modules, 
to force Maven to build this `e2e-tests` module last.
This is to enforce that, when running any of the `*IT.kt` tests, all the needed
`*.jar` executables have been packaged.

Besides `rest-assured`, to write E2E tests you will also need:

* `org.testcontainers:testcontainers`
* `org.awaitility:awaitility`

Write a test for each of the REST APIs, in which they are accessed through
the Gateway.
You will need to use TestContainers to start the `docker-compose.yml`,
and Awaitility to wait for everything being up and running before starting 
the tests.
This is particularly important to wait for Consul initialization, when all
services have been registered (otherwise, the Gateway will not be able to route
to the right running instances).  


Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-08` module.



 


 














 


 

