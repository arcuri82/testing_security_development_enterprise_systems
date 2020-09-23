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

You need to open the port for the `api-gateway`, e.g.,
```
ports:
  - "80:8080"
```
This must be the ONLY port open, and reachable from outside Docker-Compose,
i.e., all incoming communications must go through the gateway.


## E2E Tests















 


 

