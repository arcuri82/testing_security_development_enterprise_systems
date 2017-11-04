package org.tsdes.spring.security.basic

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest{

    @LocalServerPort
    private var port = 0


    @Before
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun testOpenToAll(){

        given().get("/openToAll")
                .then()
                .statusCode(200)
    }

    @Test
    fun testNotPresent(){

        /*
            Note: here we get a 401 instead of 404,
            as we are using a whitelist.
         */

        given().get("/endpointThatDoesNotExist")
                .then()
                .statusCode(401)
    }


    @Test
    fun testNotAuthenticated(){

        given().get("/forUsers")
                .then()
                .statusCode(401)
                .header("WWW-Authenticate", containsString("Basic realm"))
    }


    @Test
    fun testAuthenticatedUser(){

        given()
                /*
                    this setup the header "Authorization: Basic X", where X is
                    the user+password in Base64 format
                 */
                .auth().basic("foo","123456")
                .get("/forUsers")
                .then()
                .statusCode(200)
    }

    @Test
    fun testUnauthorized(){

        /*
            Note the difference between 401 (not authenticated)
            and 403 (authenticated, but not authorized)
         */

        given()
                .auth().basic("foo","123456")
                .get("/forAdmins")
                .then()
                .statusCode(403)
    }

    @Test
    fun testAdmin(){

        given()
                .auth().basic("admin","bar")
                .get("/forAdmins")
                .then()
                .statusCode(200)
    }


}