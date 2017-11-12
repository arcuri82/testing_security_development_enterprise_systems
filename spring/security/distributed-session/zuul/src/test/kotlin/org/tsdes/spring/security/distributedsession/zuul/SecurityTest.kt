package org.tsdes.spring.security.distributedsession.zuul

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Cookies
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
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
@ContextConfiguration(initializers = arrayOf(SecurityTest.Companion.Initializer::class))
class SecurityTest{

    @Autowired
    private lateinit var userRepository: UserRepository

    @LocalServerPort
    private var port = 0


    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule
        @JvmField
        val redis = KGenericContainer("redis:latest")
                .withExposedPorts(6379)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                val host = redis.containerIpAddress
                val port = redis.getMappedPort(6379)

                EnvironmentTestUtils.addEnvironment(
                        "testcontainers",
                        configurableApplicationContext.environment,
                        "spring.redis.host=$host",
                        "spring.redis.port=$port"
                )
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
    fun testUnauthorizedAccess(){

        given().get("/user")
                .then()
                .statusCode(401)
    }

    private fun registerUser(id: String, password: String) : Pair<String,String>{

        val xsrfToken = given().contentType(ContentType.URLENC)
                .formParam("the_user", id)
                .formParam("the_password", password)
                .post("/signIn")
                .then()
                .statusCode(403)
                .extract().cookie("XSRF-TOKEN")

        val session=  given().contentType(ContentType.URLENC)
                .formParam("the_user", id)
                .formParam("the_password", password)
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookie("XSRF-TOKEN", xsrfToken)
                .post("/signIn")
                .then()
                .statusCode(204)
                .extract().cookie("SESSION")

        return Pair(session, xsrfToken)
    }

    @Test
    fun testLogin(){

        val name = "foo"
        val pwd = "bar"

        val cookies = registerUser(name, pwd)
        val session = cookies.first

        given().get("/user")
                .then()
                .statusCode(401)

        /*
            Note: as these are gets, no need for
            setting the CSRF token
         */
        given().cookie("SESSION", session)
                .get("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("roles", contains("ROLE_USER"))


        given().auth().basic(name, pwd)
                .get("/user")
                .then()
                .statusCode(200)
                .cookie("SESSION")
                .body("name", CoreMatchers.equalTo(name))
                .body("roles", Matchers.contains("ROLE_USER"))
    }
}