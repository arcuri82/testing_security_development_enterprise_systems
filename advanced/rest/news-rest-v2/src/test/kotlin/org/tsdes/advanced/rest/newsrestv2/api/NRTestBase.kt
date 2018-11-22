package org.tsdes.advanced.rest.newsrestv2.api

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.rest.newsrestv2.NewsRestApplication
import org.tsdes.advanced.rest.newsrestv2.dto.NewsDto

/**
 * Created by arcuri82 on 14-Jul-17.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(NewsRestApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class NRTestBase {

    @LocalServerPort
    protected var port = 0

    @BeforeEach
    @AfterEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/newsrest/api/news"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        /*
           Here, we read each resource (GET), and then delete them
           one by one (DELETE)
         */
        val list = given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract()
                .`as`(Array<NewsDto>::class.java)
                .toList()


        /*
            Code 204: "No Content". The server has successfully processed the request,
            but the return HTTP response will have no body.
         */
        list.stream().forEach {
            given().pathParam("id", it.newsId)
                    .delete("/{id}")
                    .then()
                    .statusCode(204)
        }

        given().get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
    }
}