package org.tsdes.advanced.security.distributedsession.auth.db

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

/**
 * Created by arcuri82 on 10-Nov-17.
 */
@RunWith(SpringRunner::class)
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

        @ClassRule
        @JvmField
        val redis = KGenericContainer("redis:latest")
                .withExposedPorts(6379)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                val host = redis.containerIpAddress
                val port = redis.getMappedPort(6379)

                TestPropertyValues
                        .of("spring.redis.host=$host", "spring.redis.port=$port")
                        .applyTo(configurableApplicationContext.environment);
            }
        }
    }


    @Before
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
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


        val sessionCookie = given().contentType(ContentType.URLENC)
                .formParam("the_user", id)
                .formParam("the_password", password)
                .post("/signIn")
                .then()
                .statusCode(204)
                .header("Set-Cookie", not(equalTo(null)))
                .extract().cookie("SESSION")

        /*
            From now on, the user is authenticated.
            I do not need to use userid/password in the following requests.
            But each further request will need to have the SESSION cookie.
         */

        return sessionCookie
    }

    @Test
    fun testLogin() {

        val name = "foo"
        val pwd = "bar"

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
            Trying to access with login/password will reset
            the SESSION token.
         */
        given().auth().basic(name, pwd)
                .get("/user")
                .then()
                .statusCode(200)
                .cookie("SESSION") // new SESSION cookie
                .body("name", CoreMatchers.equalTo(name))
                .body("roles", Matchers.contains("ROLE_USER"))
    }
}