package org.tsdes.spring.rest.newsrest.api

import io.restassured.RestAssured.given
import io.restassured.RestAssured.get
import io.restassured.RestAssured.delete
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.tsdes.spring.rest.newsrest.dto.NewsDto
import java.time.ZonedDateTime

/**
 *  To write tests for REST APIs, a good library is RestAssured,
 *  enhanced with Hamcrest
 *
 * Created by arcuri82 on 14-Jul-17.
 */
class NewsRestApiTest : NRTestBase() {

    @Test
    fun testCleanDB() {

        given().get().then()
                .statusCode(200)
                .body("size()", equalTo(0))
    }


    @Test
    fun testCreateAndGet() {

        val author = "author"
        val text = "someText"
        val country = "Norway"
        val dto = NewsDto(null, author, text, country, null)

        given().get().then().statusCode(200).body("size()", equalTo(0))

        val id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString()

        given().get().then().statusCode(200).body("size()", equalTo(1))

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("authorId", equalTo(author))
                .body("text", equalTo(text))
                .body("country", equalTo(country))
    }


    @Test
    fun testDelete() {

        val id = given().contentType(ContentType.JSON)
                .body(NewsDto(null, "author", "text", "Norway", null))
                .post()
                .then()
                .statusCode(200)
                .extract().asString()

        get().then()
                .body("size()", equalTo(1))
                .body("id[0]", containsString(id))

        delete("/id/" + id)

        get().then().body("id", not(containsString(id)))
    }


    @Test
    @Throws(Exception::class)
    fun testUpdate() {

        val text = "someText"

        //first create with a POST
        val id = given().contentType(ContentType.JSON)
                .body(NewsDto(null, "author", text, "Norway", null))
                .post()
                .then()
                .statusCode(200)
                .extract().asString()

        //check if POST was fine
        get("/id/" + id).then().body("text", equalTo(text))

        val updatedText = "new updated text"

        //now change text with PUT
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(NewsDto(id, "foo", updatedText, "Norway", ZonedDateTime.now()))
                .put("/id/{id}")
                .then()
                .statusCode(204)

        //was the PUT fine?
        get("/id/" + id).then().body("text", equalTo(updatedText))


        //now rechange, but just the text
        val anotherText = "yet another text"

        given().contentType(ContentType.TEXT)
                .body(anotherText)
                .pathParam("id", id)
                .put("/id/{id}/text")
                .then()
                .statusCode(204)

        get("/id/" + id).then().body("text", equalTo(anotherText))
    }

    @Test
    fun testMissingForUpdate() {

        given().contentType(ContentType.JSON)
                .body("{\"id\":\"-333\"}")
                .pathParam("id", "-333")
                .put("/id/{id}")
                .then()
                .statusCode(404)
    }

    @Test
    fun testUpdateNonMatchingId() {

        given().contentType(ContentType.JSON)
                .body(NewsDto("222", "foo", "some text", "Norway", ZonedDateTime.now()))
                .pathParam("id", "-333")
                .put("/id/{id}")
                .then()
                .statusCode(409)
    }


    @Test
    fun testInvalidUpdate() {

        val id = given().contentType(ContentType.JSON)
                .body(NewsDto(null, "author", "someText", "Norway", null))
                .post()
                .then()
                .extract().asString()

        val updatedText = ""

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(NewsDto(id, null, updatedText, null, null))
                .put("/id/{id}")
                .then()
                .statusCode(400)
    }

    private fun createSomeNews() {
        createNews("a", "text", "Norway")
        createNews("a", "other text", "Norway")
        createNews("a", "more text", "Sweden")
        createNews("b", "text", "Norway")
        createNews("b", "yet another text", "Iceland")
        createNews("c", "text", "Iceland")
    }

    private fun createNews(authorId: String, text: String, country: String) {
        given().contentType(ContentType.JSON)
                .body(NewsDto(null, authorId, text, country, null))
                .post()
                .then()
                .statusCode(200)
    }

    @Test
    fun testGetAll() {

        get().then().body("size()", equalTo(0))
        createSomeNews()

        get().then().body("size()", equalTo(6))
    }

    @Test
    fun testGetAllByCountry() {

        get().then().body("size()", equalTo(0))
        createSomeNews()

        get("/countries/Norway").then().body("size()", equalTo(3))
        get("/countries/Sweden").then().body("size()", equalTo(1))
        get("/countries/Iceland").then().body("size()", equalTo(2))
    }

    @Test
    fun testGetAllByAuthor() {

        get().then().body("size()", equalTo(0))
        createSomeNews()

        get("/authors/a").then().body("size()", equalTo(3))
        get("/authors/b").then().body("size()", equalTo(2))
        get("/authors/c").then().body("size()", equalTo(1))
    }

    @Test
    fun testGetAllByCountryAndAuthor() {

        get().then().body("size()", equalTo(0))
        createSomeNews()

        get("/countries/Norway/authors/a").then().body("size()", equalTo(2))
        get("/countries/Sweden/authors/a").then().body("size()", equalTo(1))
        get("/countries/Iceland/authors/a").then().body("size()", equalTo(0))
        get("/countries/Norway/authors/b").then().body("size()", equalTo(1))
        get("/countries/Sweden/authors/b").then().body("size()", equalTo(0))
        get("/countries/Iceland/authors/b").then().body("size()", equalTo(1))
        get("/countries/Norway/authors/c").then().body("size()", equalTo(0))
        get("/countries/Sweden/authors/c").then().body("size()", equalTo(0))
        get("/countries/Iceland/authors/c").then().body("size()", equalTo(1))
    }

    @Test
    fun testInvalidGetByCountry() {

        get("/countries/foo").then().statusCode(400)
    }

    @Test
    fun testInvalidGetByCountryAndAuthor() {

        get("/countries/foo/authors/foo").then().statusCode(400)
    }


    @Test
    fun testInvalidAuthor() {

        given().contentType(ContentType.JSON)
                .body(NewsDto(null, "", "text", "Norway", null))
                .post()
                .then()
                .statusCode(400)
    }

    @Test
    fun testInvalidCountry() {

        given().contentType(ContentType.JSON)
                .body(NewsDto(null, "author", "text", "foo", null))
                .post()
                .then()
                .statusCode(400)
    }

    @Test
    fun testPostWithId() {
        given().contentType(ContentType.JSON)
                .body(NewsDto("1", "author", "text", "Norway", null))
                .post()
                .then()
                .statusCode(400)
    }

    @Test
    fun testPostWithWrongType() {

        /*
            HTTP Error 415: "Unsupported media type"
            The REST API is set to receive data in JSon, ie
            "consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE)"
            so, if send XML, we should get a 415 error.
            Note: a server might provide and receive the same
            resource (on same URL) with different formats!
            Although nowadays most just deal with Json.
         */

        given().contentType(ContentType.XML)
                .body("<foo></foo>")
                .post()
                .then()
                .statusCode(415)
    }


    @Test
    fun testGetByInvalidId() {

        /*
            In this particular case, "foo" might be a valid id.
            however, as it is not in the database, and there is no mapping
            for a String id, the server will say "Not Found", ie 404.
         */

        get("/id/foo")
                .then()
                .statusCode(404)
    }

    /*
        Test if Swagger is properly configured
     */

    @Test
    fun testSwaggerSchema(){

        get("../v2/api-docs")
                .then()
                .statusCode(200)
                .body("swagger", equalTo("2.0"))
    }

    @Test
    fun testSwaggerUI(){
        get("../swagger-ui.html").then().statusCode(200)
    }
}