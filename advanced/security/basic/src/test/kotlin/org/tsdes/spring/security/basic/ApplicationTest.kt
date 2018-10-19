package org.tsdes.spring.security.basic

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @LocalServerPort
    private var port = 0


    @Before
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testOpenToAll() {

        given().get("/openToAll")
                .then()
                .statusCode(200)
    }

    @Test
    fun testNotPresent() {

        /*
            Note: here we get a 401 instead of 404,
            as we are using a whitelist.
         */

        given().get("/endpointThatDoesNotExist")
                .then()
                .statusCode(401)
    }


    @Test
    fun testNotAuthenticated() {

        /*
            Here, we get a 401 not authenticated.
            Server also needs to tell us how authentication is done, like for
            example using "Basic" protocol.
            The server could have several domains of authentication, each one with
            its own system and different users/passwords.
            The "realm" specifies which domain is involved in the requested resource.
            If we do not configure it in Spring Security, it will just choose a default
            name like "Realm".
         */

        given().get("/forUsers")
                .then()
                .statusCode(401)
                .header("WWW-Authenticate", containsString("Basic realm=\"Realm\""))
    }


    @Test
    fun testAuthenticatedUser() {

        given()
                /*
                    this setup the header "Authorization: Basic X", where X is
                    the user:password in Base64 format.
                    You could set it up manually by doing a
                    .header("Authorization", "Basic " + X)
                    but then would need to convert manually to Base64 (or use library for it).
                    RestAssured does it automatically for you.
                 */
                .auth().basic("foo", "123456")
                .get("/forUsers")
                .then()
                .statusCode(200)
    }

    @Test
    fun testUnauthorized() {

        /*
            Note the difference between 401 (not authenticated)
            and 403 (authenticated, but not authorized)
         */

        given().auth().basic("foo", "123456")
                .get("/forAdmins")
                .then()
                .statusCode(403)
    }

    @Test
    fun testAdmin() {

        given().auth().basic("admin", "bar")
                .get("/forAdmins")
                .then()
                .statusCode(200)
    }


}