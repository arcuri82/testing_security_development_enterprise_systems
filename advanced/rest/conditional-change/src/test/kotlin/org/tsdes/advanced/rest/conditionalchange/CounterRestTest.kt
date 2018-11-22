package org.tsdes.advanced.rest.conditionalchange

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by arcuri82 on 30-Aug-18.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ConditionalChangeApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounterRestTest {

    @LocalServerPort
    protected var port = 0

    @BeforeEach
    fun init() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/conditionalCounter/api/counter"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }


    @Test
    fun testCreateAndGet() {

        val name = "a name"
        val value = 42

        given().contentType(ContentType.JSON)
                .body(CounterDto(name, value))
                .put()
                .then()
                .statusCode(204)

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("value", equalTo(value))
    }


    @Test
    fun testOptimisticLocking() {

        val name = "a name"
        val value = 42

        given().contentType(ContentType.JSON)
                .body(CounterDto(name, value))
                .put()
                .then()
                .statusCode(204)

        //we read the value, with a given ETag
        val response = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("value", equalTo(value))

        val etag = response.extract().header("ETag")
        val k = response.extract().path<Int>("value")

        /*
            we want to increase the counter by 1, but, before we can
            do it, we simulate someone else doing a modification between
            our GET and PUT (which are not done together as an atomic action).
            Note that this will change the ETag
         */
        given().contentType(ContentType.JSON)
                .body(CounterDto(name, value*4))
                .put()
                .then()
                .statusCode(204)

        /*
            now, we want to do the increase by 1, but only if there
            was no modification since our GET. We do this by specifying
            the ETag from the GET
         */
        given().contentType(ContentType.JSON)
                .header("If-Match", etag)
                .body(CounterDto(name, k+1))
                .put()
                .then()
                //pre-condition should fail, as ETag has been changed since the GET
                .statusCode(412)

        /*
            Note: what described here can be considered like an "optimistic lock",
            in similar concept as in JPA for databases.
            We want a read+write operation done atomically, but, instead of locking
            the server on the GET (which should not be done in HTTP), we just throw
            an error if we detect that the state was changed by someone else
            before we do the PUT/POST.

            Note: this is just a simple example. In this particular case of just increasing
            a value by 1, a PATCH would had been a better solution...
         */
    }
}