package org.tsdes.advanced.rest.conditionalget

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by arcuri82 on 27-Aug-18.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ConditionalGetApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalGetRestTest{

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var repository: NewsRepository


    @Before
    fun init() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/conditionalNews/api/news"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        repository.reset()
    }

    @Test
    fun testEtag(){

        val etag = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
                .header("ETag", notNullValue())
                .header("last-modified", notNullValue())
                .extract().header("ETag")

        /*
            As we used the same ETag, the server should not reply with
            a payload.
            However, it is our job to save the previous answer (which we
            did not), ie we need a client-side cache.
         */
        given().accept(ContentType.JSON)
                .header("If-None-Match", etag)
                .get()
                .then()
                .statusCode(304)
                .content(equalTo(""))
    }

    @Test
    fun testEtagAfterUpdate(){

        val etag = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
                .header("ETag", notNullValue())
                .header("last-modified", notNullValue())
                .extract().header("ETag")

        given().accept(ContentType.JSON)
                .header("If-None-Match", etag)
                .get()
                .then()
                .statusCode(304)
                .content(equalTo(""))

        //this will update, and create a new Etag
        given().contentType(ContentType.JSON)
                .body("hello")
                .post()
                .then()
                .statusCode(201)

        /*
            We try to use the old ETag.
            As the resource has changed, we get the new representation
            with the new ETag.
         */
        given().accept(ContentType.JSON)
                .header("If-None-Match", etag)
                .get()
                .then()
                .statusCode(200)
                .content(not(equalTo("")))
                .body("size()", equalTo(1))
                .header("ETag", notNullValue())
                .header("ETag", not(equalTo(etag)))
    }


    @Test
    fun tetLastModified(){

        val lastmodified = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
                .header("ETag", notNullValue())
                .header("last-modified", notNullValue())
                .extract().header("last-modified")

        /*
            As we used the same timestamp, the server should not reply with
            a payload, as no new changes were made meanwhile.
            However, it is our job to save the previous answer (which we
            did not), ie we need a client-side cache.

            Note: it is important that we do NOT use the clock of the client machine,
            and actually use the time we received from "last-modified" header.
            Point is, client and server machines most likely have clocks NOT in sync.
         */
        given().accept(ContentType.JSON)
                .header("If-Modified-Since", lastmodified)
                .get()
                .then()
                .statusCode(304)
                .content(equalTo(""))
    }

    @Test
    fun tetLastModifiedAfterUpdate(){

        val lastmodified = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
                .header("ETag", notNullValue())
                .header("last-modified", notNullValue())
                .extract().header("last-modified")

        given().accept(ContentType.JSON)
                .header("If-Modified-Since", lastmodified)
                .get()
                .then()
                .statusCode(304)
                .content(equalTo(""))

        /*
            HTTP Dates have 1 second resolution, not milliseconds.
            So, here we wait at least 2 seconds to make sure that the
            newly generated news has a different time-stamp when converted
            into a HTTP Date by Spring.
         */
        Thread.sleep(2_000)

        given().contentType(ContentType.JSON)
                .body("hello")
                .post()
                .then()
                .statusCode(201)

        given().accept(ContentType.JSON)
                .header("If-Modified-Since", lastmodified)
                .get()
                .then()
                .statusCode(200)
                .content(not(equalTo("")))
                .body("size()", equalTo(1))
                .header("last-modified", notNullValue())
                .header("last-modified", not(equalTo(lastmodified)))
    }
}