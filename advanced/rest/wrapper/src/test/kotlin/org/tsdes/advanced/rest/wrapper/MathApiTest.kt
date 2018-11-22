package org.tsdes.advanced.rest.wrapper

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.advanced.rest.dto.WrappedResponse.ResponseStatus.ERROR
import org.tsdes.advanced.rest.dto.WrappedResponse.ResponseStatus.SUCCESS


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(MathApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MathApiTest{


    @LocalServerPort
    protected var port = 0


    @BeforeEach
    fun initTest() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/math/divide"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }


    @Test
    fun testSuccessfulDivision(){

        val x = 10
        val y = 2
        val expected = x / y

        given().accept(ContentType.JSON)
                .param("x", x)
                .param("y", y)
                .get()
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("status", equalToIgnoringCase(SUCCESS.toString()))
                .body("data.result", equalTo(expected))
                .body("message", equalTo(null))
    }

    @Test
    fun testUnmarshalling(){

        val url = "http://localhost:$port/math/divide?x=10&y=2"
        val client =  RestTemplate()


        val responseSubclass = client.getForEntity(url, ResponseDto::class.java)

        assertEquals(200, responseSubclass.statusCode.value())
        assertEquals(5, responseSubclass.body.data!!.result)


        //dealing with Generics is slightly bit more cumbersome
        val responseGenerics = client.exchange(
                url,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<WrappedResponse<DivisionDto>>() {})

        assertEquals(200, responseGenerics.statusCode.value())
        assertEquals(5, responseGenerics.body.data!!.result)
    }

    @Test
    fun testDivideByZero(){

        given().accept(ContentType.JSON)
                .param("x", 10)
                .param("y", 0)
                .get()
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("status", equalToIgnoringCase(ERROR.toString()))
                .body("message", containsString("Cannot divide"))
    }

    @Test
    fun testMissingParam(){

        /*
            Here, the validation is done by Spring, and fails before
            the API code is even called.
            Spring sends a response with JSON data, with its own format.
            To make it consistent with our own format of the wrapped,
            we will need to customize how exceptions are handled
         */

        given().accept(ContentType.JSON)
                .param("x", 10)
                .get()
                .then()
                .statusCode(400)
                //note here that "status" represent the HTTP code
                .body("status", equalTo(400))
                .body("message", not(equalTo(null)))

        /*
            {
                "timestamp": "2018-07-14T12:06:22.906+0000",
                "status": 400,
                "error": "Bad Request",
                "message": "Required int parameter 'y' is not present",
                "path": "/math/divide"
            }
         */
    }
}