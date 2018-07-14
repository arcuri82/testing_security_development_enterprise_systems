package org.tsdes.advanced.rest.redirect

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.spring.rest.patch.CounterDto

/**
 * Created by arcuri82 on 01-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounterRestTest {

    @LocalServerPort
    private var port = 0

    @Before
    fun initTestSuite() {
        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/redirect/api/counters"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        //clean counters
        given().delete()
                .then()
                .statusCode(204)

        given().get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
    }


    @Test
    fun testNoLatest() {

        given().get("latest")
                .then()
                .statusCode(409)
    }

    @Test
    fun testPermanentRedirect() {

        /*
            NOTE: by default, RestAssured will automatically
            follow the redirects
         */

        given().redirects().follow(false)
                .get("theLatest")
                .then()
                .statusCode(301)
                .header("Location", containsString("latest"))


        //automated re-direct to "latest", but now there is none
        given().accept(ContentType.JSON)
                .get("theLatest")
                .then()
                .statusCode(409)
    }


    @Test
    fun testFindLatest() {

        val id = createNew()

        val location = given()
                .redirects().follow(false)
                .get("latest")
                .then()
                .statusCode(307)
                .extract()
                .header("Location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))


        //with auto-redirect, equivalent to:
        given().accept(ContentType.JSON)
                .get("latest")
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
    }

    @Test
    fun testCreate2AndFindLatest() {

        val first = createNew()
        val second = createNew()

        val location = given()
                .redirects().follow(false)
                .get("latest")
                .then()
                .statusCode(307)
                .extract()
                .header("Location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("id", not(equalTo(first)))
                .body("id", equalTo(second))
    }


    @Test
    fun testDoubleRedirection() {

        val id = createNew()

        var location = given()
                .redirects().follow(false)
                .get("theLatest")
                .then()
                .statusCode(301)
                .extract()
                .header("Location")

        location = given()
                .redirects().follow(false)
                .basePath("")
                .get(location)
                .then()
                .statusCode(307)
                .extract()
                .header("Location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))


        //the above is equivalent to:
        given().accept(ContentType.JSON)
                .get("theLatest")
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
    }


    private fun createNew(): Long {

        val id = System.currentTimeMillis()
        val dto = CounterDto()
        dto.id = id

        given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)

        return id
    }
}