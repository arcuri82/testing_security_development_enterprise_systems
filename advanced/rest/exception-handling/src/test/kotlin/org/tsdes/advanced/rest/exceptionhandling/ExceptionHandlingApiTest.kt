package org.tsdes.advanced.rest.exceptionhandling

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.advanced.rest.dto.WrappedResponse.ResponseStatus.ERROR
import org.tsdes.advanced.rest.dto.WrappedResponse.ResponseStatus.SUCCESS
import org.tsdes.advanced.rest.dto.WrappedResponse.ResponseStatus.FAIL

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(ExceptionHandlingApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExceptionHandlingApiTest{

    @LocalServerPort
    protected var port = 0


    @Before
    fun initTest() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }



    @Test
    fun testCorrectDivide(){

        val x = 10
        val y = 2
        val expected = x / y

        listOf("v0", "v1", "v2").forEach{

            given().accept(ContentType.JSON)
                    .param("x", x)
                    .param("y", y)
                    .get("/divide_$it")
                    .then()
                    .statusCode(200)
                    .body("code", equalTo(200))
                    .body("status",equalToIgnoringCase(SUCCESS.toString()))
                    .body("data.result", equalTo(expected))
                    .body("message", equalTo(null))
        }
    }


    @Test
    fun testFailV0(){

        given().accept(ContentType.JSON)
                .param("x", 10)
                .param("y", 0)
                .get("/divide_v0")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("Cannot divide"))

    }


    @Test
    fun testFailV1(){

        given().accept(ContentType.JSON)
                .param("x", 10)
                .param("y", 0)
                .get("/divide_v1")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("Cannot divide"))
    }

    @Test
    fun testServerBug(){

        given().accept(ContentType.JSON)
                .get("/server_bug")
                .then()
                .statusCode(500)
                .body("code", equalTo(500))
                .body("status", equalToIgnoringCase(FAIL.toString()))
                .body("message", containsString("A simulated bug"))
    }


    @Test
    fun testFailV2(){

        given().accept(ContentType.JSON)
                .param("x", 10)
                .param("y", 0)
                .get("/divide_v2")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("Value must not be zero"))
    }

    @Test
    fun testMissingParam(){

        /*
           Now, we have override default in Spring, and use our own JSON
         */

        given().accept(ContentType.JSON)
                .param("x", 10)
                .get("/divide_v0")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("Required int parameter 'y' is not present"))
    }


    @Test
    fun testCreateUser(){

        given().accept(ContentType.JSON)
                .queryParam("name", "Foo")
                .queryParam("age", 18)
                .post("/users")
                .then()
                .statusCode(201)
                .body("code", equalTo(201))
                .body("status", equalToIgnoringCase(SUCCESS.toString()))
                .body("data", not(equalTo(null)))
    }

    @Test
    fun testFailCreateUser(){

        given().accept(ContentType.JSON)
                .queryParam("name", "")
                .queryParam("age", -5)
                .post("/users")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("name"))
                .body("message", containsString("age"))
                //we shouldn't leak implementation details
                .body("message", not(containsString("org.tsdes.advanced")))
    }


    @Test
    fun testNotFound(){

        /*
            This needs special settings in application.properties to work
         */

        given().accept(ContentType.JSON)
                .get("/doesNotExist")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("No handler found"))
    }
}