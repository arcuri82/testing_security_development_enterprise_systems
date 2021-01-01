package org.tsdes.advanced.rest.newsrest.api

import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import org.tsdes.advanced.rest.newsrest.dto.NewsDto
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
                .statusCode(201)
                .extract().asString()

        given().get().then().statusCode(200).body("size()", equalTo(1))

        given().pathParam("id", id)
                .get("/{id}")
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
                .statusCode(201)
                .extract().asString()

        get().then()
                .body("size()", equalTo(1))
                .body("id[0]", containsString(id))

        delete("/$id").then().statusCode(204)

        get().then().body("id", not(containsString(id)))

        //delete again
        delete("/$id").then().statusCode(404) //note the change from 204 to 404
    }


    @Test
    fun testUpdate() {

        val text = "someText"

        //first create with a POST
        val id = given().contentType(ContentType.JSON)
                .body(NewsDto(null, "author", text, "Norway", null))
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        //check if POST was fine
        get("/$id").then().body("text", equalTo(text))

        val updatedText = "new updated text"

        //now change text with PUT
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(NewsDto(id, "foo", updatedText, "Norway", ZonedDateTime.now()))
                .put("/{id}")
                .then()
                .statusCode(204)

        //was the PUT fine?
        get("/$id").then().body("text", equalTo(updatedText))


        //now rechange, but just the text
        val anotherText = "yet another text"

        given().contentType(ContentType.TEXT)
                .body(anotherText)
                .pathParam("id", id)
                .put("/{id}/text")
                .then()
                .statusCode(204)

        get("/$id").then().body("text", equalTo(anotherText))
    }

    @Test
    fun testMissingForUpdate() {

        given().contentType(ContentType.JSON)
                .body("{\"id\":\"-333\"}")
                .pathParam("id", "-333")
                .put("/{id}")
                .then()
                .statusCode(404)
    }

    @Test
    fun testUpdateNonMatchingId() {

        given().contentType(ContentType.JSON)
                .body(NewsDto("222", "foo", "some text", "Norway", ZonedDateTime.now()))
                .pathParam("id", "-333")
                .put("/{id}")
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
                .put("/{id}")
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
                .statusCode(201)
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

        get("?country=Norway").then().body("size()", equalTo(3))
        get("?country=Sweden").then().body("size()", equalTo(1))
        get("?country=Iceland").then().body("size()", equalTo(2))
    }

    @Test
    fun testGetAllByAuthor() {

        get().then().body("size()", equalTo(0))
        createSomeNews()

        given().queryParam("authorId", "a")
                .get()
                .then().body("size()", equalTo(3))
        get("?authorId=b").then().body("size()", equalTo(2))
        get("?authorId=c").then().body("size()", equalTo(1))
    }

    @Test
    fun testGetAllByCountryAndAuthor() {

        get().then().body("size()", equalTo(0))
        createSomeNews()

        get("?country=Norway&authorId=a").then().body("size()", equalTo(2))
        get("?country=Sweden&authorId=a").then().body("size()", equalTo(1))
        get("?country=Iceland&authorId=a").then().body("size()", equalTo(0))
        get("?country=Norway&authorId=b").then().body("size()", equalTo(1))
        get("?country=Sweden&authorId=b").then().body("size()", equalTo(0))
        get("?country=Iceland&authorId=b").then().body("size()", equalTo(1))
        get("?country=Norway&authorId=c").then().body("size()", equalTo(0))
        get("?country=Sweden&authorId=c").then().body("size()", equalTo(0))
        get("?country=Iceland&authorId=c").then().body("size()", equalTo(1))
    }

    @Test
    fun testInvalidGetByCountry() {

        /*
            Although the fields are marked with constraint @Country,
            by default Spring does not validate them.
            We will see later of to handle constraint validations
         */

        given().queryParam("country", "foo")
                .get().then()
                .statusCode(200)
                .body("size()", equalTo(0))
    }

    @Test
    fun testInvalidGetByCountryAndAuthor() {

        given().queryParam("country", "foo")
                .queryParam("authorId", "foo")
                .get().then()
                .statusCode(200)
                .body("size()", equalTo(0))
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
            The REST API is set to receive data in JSON, ie
            "consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE)"
            so, if send XML, we should get a 415 error.
            Note: a server might provide and receive the same
            resource (on same URL) with different formats!
            Although nowadays most just deal with JSON.
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

        get("/foo")
                .then()
                .statusCode(404)
    }

    /*
        Test if Swagger is properly configured
     */

    @Test
    fun testSwaggerSchemaV2(){

        given().basePath("")
                .get("/newsrest/api/v2/api-docs")
                .then()
                .statusCode(200)
                .body("swagger", equalTo("2.0"))
    }

    @Test
    fun testSwaggerSchemaV3(){

        given().basePath("")
                .get("/newsrest/api/v3/api-docs")
                .then()
                .statusCode(200)
                .body("openapi", equalTo("3.0.3"))
    }

    @Test
    fun testSwaggerUI(){
        given().basePath("")
                .get("/newsrest/api/swagger-ui/index.html")
                .then()
                .statusCode(200)
    }
}