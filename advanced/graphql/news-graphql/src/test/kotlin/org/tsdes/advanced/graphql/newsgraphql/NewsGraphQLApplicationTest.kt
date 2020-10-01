package org.tsdes.advanced.graphql.newsgraphql

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.advanced.examplenews.NewsRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewsGraphQLApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsGraphQLApplicationTest{

    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var crud: NewsRepository


    @BeforeEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/graphql"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        crud.deleteAll()
    }


    @Test
    fun testCountries(){

        given().accept(ContentType.JSON)
                .queryParam("query", "{countries}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.countries.size()", greaterThan(200))
                .body("data.countries", hasItem("Norway"))
                .body("data.countries", hasItem("Sweden"))
                .body("data.countries", hasItem("Germany"))
    }


    @Test
    fun testGetMissing(){

        given().accept(ContentType.JSON)
                .queryParam("query", "{newsById(id:\"-1\"){newsId, authorId, text, country}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById", equalTo(null))
    }

    @Test
    fun testCreateAndGet() {

        val author = "author"
        val text = "someText"
        val country = "Norway"

        val startTime = ZonedDateTime.now()

        //no news
        given().accept(ContentType.JSON)
                .queryParam("query", "{news{newsId}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.news.size()", equalTo(0))


        //create a news
        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{createNews(news:{authorId:\"$author\",text:\"$text\",country:\"$country\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .extract().body().path<String>("data.createNews")



        //should be 1 news now
        given().accept(ContentType.JSON)
                .queryParam("query", "{news{newsId}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.news.size()", equalTo(1))


        //1 news with same data as the Mutation operation
        val creationTimeString = given().accept(ContentType.JSON)
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country, creationTime}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById.newsId", equalTo(id))
                .body("data.newsById.authorId", equalTo(author))
                .body("data.newsById.text", equalTo(text))
                .body("data.newsById.country", equalTo(country))
                .extract().body().path<String>("data.newsById.creationTime")

        val creationTime = ZonedDateTime.parse(creationTimeString)

        val endTime = ZonedDateTime.now()

        assertTrue(creationTime.isAfter(startTime))
        assertTrue(creationTime.isBefore(endTime))
    }


    @Test
    fun testCreateWithMissingFields(){

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{createNews(news:{text:\"some text\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("data.createNews", equalTo(null))
                .body("errors.message[0]", containsString("error"))
                .body("errors.message[0]", containsString("country"))
                .body("errors.message[0]", containsString("authorId"))
    }

    @Test
    fun testCreateWithFailedDBValidation(){

        val wrongCountry = "foo"

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{createNews(news:{authorId:\"1\", text:\"some text\", country:\"$wrongCountry\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("data.createNews", equalTo(""))
                .body("errors.message[0]", containsString("Violated constraints"))
    }


    @Disabled
    @Test
    fun testUpdate() {

        val author = "author"
        val text = "someText"
        val country = "Norway"

        //create a news
        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{createNews(news:{authorId:\"$author\",text:\"$text\",country:\"$country\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .extract().body().path<String>("data.createNews")


        val updatedText = "updated text"
        val updatedTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

        //update its value
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{updateNewsById(id:\"$id\", news:{authorId:\"$author\",text:\"$updatedText\",country:\"$country\",creationTime:\"$updatedTime\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("data.updateNewsById", equalTo(true))

        //should be able to read it back
        given().accept(ContentType.JSON)
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country, creationTime}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById.newsId", equalTo(id))
                .body("data.newsById.authorId", equalTo(author))
                .body("data.newsById.text", equalTo(updatedText))
                .body("data.newsById.country", equalTo(country))
                .body("data.newsById.creationTime", equalTo(updatedTime))
    }

    @Test
    fun testDelete(){

        val author = "author"
        val text = "someText"
        val country = "Norway"

        //create a news
        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{createNews(news:{authorId:\"$author\",text:\"$text\",country:\"$country\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .extract().body().path<String>("data.createNews")


        //should be able to read it back
        given().accept(ContentType.JSON)
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country, creationTime}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById.newsId", equalTo(id))
                .body("data.newsById.authorId", equalTo(author))
                .body("data.newsById.text", equalTo(text))
                .body("data.newsById.country", equalTo(country))


        //now, let's delete it
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{deleteNewsById(id:\"$id\")}"}
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("data.deleteNewsById", equalTo(true))


        //should NOT be able to read it back once deleted
        given().accept(ContentType.JSON)
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country, creationTime}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById", equalTo(null))
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

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{createNews(news:{authorId:\"$authorId\",text:\"$text\",country:\"$country\"})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
    }

    @Test
    fun testGetAll() {

        given().queryParam("query", "{news{newsId}}").get()
                .then().body("data.news.size()", equalTo(0))

        createSomeNews()

        given().queryParam("query", "{news{newsId}}").get()
                .then().body("data.news.size()", equalTo(6))
    }

    @Test
    fun testGetAllByCountry() {

        createSomeNews()

        given().queryParam("query", "{news(country:\"Norway\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(3))
        given().queryParam("query", "{news(country:\"Sweden\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(1))
        given().queryParam("query", "{news(country:\"Iceland\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(2))
    }

    @Test
    fun testGetAllByAuthor() {

        createSomeNews()

        given().queryParam("query", "{news(authorId:\"a\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(3))
        given().queryParam("query", "{news(authorId:\"b\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(2))
        given().queryParam("query", "{news(authorId:\"c\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(1))
    }

    @Test
    fun testGetAllByCountryAndAuthor() {

        createSomeNews()

        given().queryParam("query", "{news(country:\"Norway\",authorId:\"a\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(2))
        given().queryParam("query", "{news(country:\"Sweden\",authorId:\"a\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(1))
        given().queryParam("query", "{news(country:\"Iceland\",authorId:\"a\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(0))
        given().queryParam("query", "{news(country:\"Norway\",authorId:\"b\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(1))
        given().queryParam("query", "{news(country:\"Sweden\",authorId:\"b\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(0))
        given().queryParam("query", "{news(country:\"Iceland\",authorId:\"b\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(1))
        given().queryParam("query", "{news(country:\"Norway\",authorId:\"c\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(0))
        given().queryParam("query", "{news(country:\"Sweden\",authorId:\"c\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(0))
        given().queryParam("query", "{news(country:\"Iceland\",authorId:\"c\"){newsId}}").get()
                .then().body("data.news.size()", equalTo(1))
    }
}