package org.tsdes.advanced.exercises.cardgame.auth

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.tsdes.advanced.exercises.cardgame.auth.db.UserRepository

/**
 * Created by arcuri82 on 10-Nov-17.
 */
@ActiveProfiles("test")
@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [(SecurityTest.Companion.Initializer::class)])
class SecurityTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @LocalServerPort
    private var port = 0


    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        /*
            Here, going to use an actual Redis instance started in Docker
         */

        @Container
        @JvmField
        val redis = KGenericContainer("redis:latest").withExposedPorts(6379)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                TestPropertyValues
                        .of("spring.redis.host=${redis.containerIpAddress}",
                                "spring.redis.port=${redis.getMappedPort(6379)}")
                        .applyTo(configurableApplicationContext.environment);
            }
        }
    }


    @BeforeEach
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.basePath = "/api/auth"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        userRepository.deleteAll()
    }


    @Test
    fun testUnauthorizedAccess() {

        given().get("/user")
                .then()
                .statusCode(401)
    }


    /**
     *   Utility function used to create a new user in the database
     */
    private fun registerUser(id: String, password: String): String {


        val sessionCookie = given().contentType(ContentType.JSON)
                .body(AuthDto(id, password))
                .post("/signUp")
                .then()
                .statusCode(201)
                .header("Set-Cookie", not(equalTo(null)))
                .extract().cookie("SESSION")

        /*
            From now on, the user is authenticated.
            I do not need to use userid/password in the following requests.
            But each further request will need to have the SESSION cookie.
         */

        return sessionCookie
    }

    private fun checkAuthenticatedCookie(cookie: String, expectedCode: Int){
        given().cookie("SESSION", cookie)
                .get("/user")
                .then()
                .statusCode(expectedCode)
    }

    @Test
    fun testLogin() {

        val name = "foo"
        val pwd = "bar"

        checkAuthenticatedCookie("invalid cookie", 401)

        val cookie = registerUser(name, pwd)

        given().get("/user")
                .then()
                .statusCode(401)

        given().cookie("SESSION", cookie)
                .get("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("roles", contains("ROLE_USER"))



        /*
            Trying to login again will reset
            the SESSION token.
         */
        val login = given().contentType(ContentType.JSON)
                .body(AuthDto(name, pwd))
                .post("/login")
                .then()
                .statusCode(204)
                .cookie("SESSION") // new SESSION cookie
                .extract().cookie("SESSION")

        assertNotEquals(login, cookie)
        checkAuthenticatedCookie(login, 200)
    }



    @Test
    fun testWrongLogin() {

        val name = "foo"
        val pwd = "bar"

        val noAuth = given().contentType(ContentType.JSON)
                .body(AuthDto(name, pwd))
                .post("/login")
                .then()
                .statusCode(400)
                .extract().cookie("SESSION")

        //session is not created if not required, eg when 400 user error
        assertNull(noAuth)

        registerUser(name, pwd)

        val auth = given().contentType(ContentType.JSON)
                .body(AuthDto(name, pwd))
                .post("/login")
                .then()
                .statusCode(204)
                .extract().cookie("SESSION")

        checkAuthenticatedCookie(auth, 200)
    }
}