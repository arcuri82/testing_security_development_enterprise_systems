# Card Game - Part 02

In the `cards-dto` module, add a dependency to 
`io.springfox:springfox-boot-starter`.
Add documentation (e.g., `@ApiModelProperty`) to all the DTOs.


In the `user-collections` module, you need to add the following dependencies:
* `org.springframework.boot:spring-boot-starter-web`
* `io.springfox:springfox-boot-starter`
* `io.rest-assured:rest-assured`

Add/update the following classes:
* `Applicaton`, enable the bean to initialize SpringFox documentation.
* `dto/UserDto`, DTO for the entity `User`.
* `dto/CardCopyDto`, DTO for the entity `CardCopy`.
* `DtoConverter`, utility class for converting from JPA entity to DTO.
* `RestAPI`, the API implemented in a `@RestController`.

The REST API needs to have the following endpoints:
* `GET /api/user-collections/{userId}`, to retrieve info on a user with
    the given id.
* `PUT /api/user-collections/{userId}`, to create a new user info, with
    default values and the given id. This PUT expects no body payload
    as input.
    
All endpoints must satisfy the semantics of HTTP (e.g., return a 404 when trying
to retrieve a user that does not exist), and must be documented with SpringFox.
 
For testing, create a `RestAPITest` test suite, with at least test
one test case per endpoint, using RestAssured. 

Make sure that your `application.yml` has a proper value for 
`spring.application.name` (e.g., `user-collections-service`), and that you are using the
 `spring-boot-maven-plugin` to create a uber-jar.

When running the application, it will fail, unless you have Postgres up and running on
your machine.
Under `src/test/kotlin`, create a `LocalApplicationRunner` class
in which the application is rather started with H2
(e.g., recall you can activate profiles with `--spring.profiles.active`).
When it is started from an IDE (e.g., IntelliJ), then you should be 
able to navigate the documentation by opening:
`http://localhost:8080/swagger-ui/index.html`.


Solutions to this exercise can be found in the 
`advanced/exercise-solutions/card-game/part-02` module.
        


