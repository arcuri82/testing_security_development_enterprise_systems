# Card Game - Part 10

## Services

The services `auth`, `scores` and `user-collections` will need to use
RabbitMQ.
Add the dependency `org.springframework.boot:spring-boot-starter-amqp`,
and make sure to configure the connection to RabbitMQ in the `application.yml` file.

In `auth`, every time a new user does a successful `signup`,
the service should do a broadcast on a fanout, sending the user's ID.
Update the tests to start a RabbitMQ instance with TestContainers.

Both `scores` and `user-collections` should be listeners on these
sign-up events.
Every time a new user signs-up, those services should create new user info
for them, i.e., equivalent to calling directly the endpoints:
* `PUT /api/user-collections/{userId}`
* `PUT /api/scores/{userId}`

Note: although it is a broadcast, only one instance of `scores` and 
only one instance of `user-collections` should receive and process such
messages (in case you have several replicated instances in your Docker-Compose file).
This means you need to use a named queue for each service kind
(i.e., a named queue for all instances of `scores` that is different 
from the named queue for all instances of `user-collections`).
   

## Docker-Compose

In the `docker-compose.yml` file, start an instance of RabbitMQ.
All services using RabbitMQ will need to be marked with `depends_on`.


## E2E Tests

Create a new E2E test, to check that, once a new user has signed-up,
within a certain amount of time it is possible to directly get info on such user
from the `scores` and `user-collections` services (as those will be
notified via RabbitMQ, eventually). 

Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-10` module.