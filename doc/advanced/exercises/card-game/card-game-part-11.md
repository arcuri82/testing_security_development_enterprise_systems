# Card Game - Part 11

Create a web frontend for the application, in which
the main functionalities of the system can be executed
from a browser. For example:

* Signup/login/logout users
* Show all cards in the game
* Show cards belonging to the logged-in user (and their quantities, if having more than one copy)
* Open card-packs
* Paginated leader-board

There is no restriction on how to build the frontend, e.g., using
something like a SPA with *React*, *Vue* or vanilla JavaScript.

The static assets (HTML, CSS, JS, images, etc.) need to be served by 
a new service. 
It can be a NodeJS instance, or something like an Apache Server.

Add such service to the `docker-compose.yml` file, and make sure to update
the API Gateway with a new route for this service. 

**Suggestion**: if you are using NodeJS, you really want to have 
a "dev" mode to instruct  NodeJS to proxy all API calls (i.e., the ones to */api/**) to the API Gateway,
using libraries like *http-proxy-middleware*.
The idea is that you can run the frontend outside of Docker-Compose, while still running
the entire microservice on the side (so, technically there would be 2 instances of the frontend:
one inside and one outside Docker Compose).
By doing this, you can fix minor things in the frontend (e.g., CSS), without at each change
having to rebuild and restart the whole microservice...

 
Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-11` module.
  
 
