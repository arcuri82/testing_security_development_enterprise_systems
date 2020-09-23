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


## 

