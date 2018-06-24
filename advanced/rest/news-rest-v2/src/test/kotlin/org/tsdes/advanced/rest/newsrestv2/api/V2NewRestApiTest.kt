package org.tsdes.advanced.rest.newsrestv2.api

import io.restassured.RestAssured.delete
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.tsdes.advanced.rest.newsrestv2.dto.NewsDto

/**
 * Created by arcuri82 on 01-Aug-17.
 */
class V2NewRestApiTest : NRTestBase() {

    @Test
    fun testCreateAndGetWithNewFormat() {

        val author = "author"
        val text = "someText"
        val country = "Norway"
        val dto = NewsDto(null, author, text, country, null)

        //no news
        given().contentType(V2_NEWS_JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))

        //create a news
        val id = given().contentType(V2_NEWS_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        //should be 1 news now
        given().contentType(V2_NEWS_JSON)
                .get()
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))

        //1 news with same data as the POST
        given().accept(V2_NEWS_JSON)
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("newsId", equalTo(id))
                .body("authorId", equalTo(author))
                .body("text", equalTo(text))
                .body("country", equalTo(country))
    }

    @Test
    fun testDoubleDelete() {

        val dto = NewsDto(null, "author", "text", "Norway", null)

        //create a news
        val id = given().contentType(V2_NEWS_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        delete("/" + id).then().statusCode(204)

        //delete again
        delete("/" + id).then().statusCode(404) //note the change from 204 to 404
    }
}