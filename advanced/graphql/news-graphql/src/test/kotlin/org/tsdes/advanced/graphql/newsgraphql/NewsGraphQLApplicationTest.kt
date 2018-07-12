package org.tsdes.advanced.graphql.newsgraphql

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.graphql.newsgraphql.type.InputNewsType


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [NewsGraphQLApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsGraphQLApplicationTest{

    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var crud: NewsRepository


    @Before
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
        val dto = InputNewsType(author, text, country)

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
        given().accept(ContentType.JSON)
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById.newsId", equalTo(id))
                .body("data.newsById.authorId", equalTo(author))
                .body("data.newsById.text", equalTo(text))
                .body("data.newsById.country", equalTo(country))
    }


    //TODO invalid inputs

    @Test
    fun testDelete(){

        val author = "author"
        val text = "someText"
        val country = "Norway"
        val dto = InputNewsType(author, text, country)


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
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById.newsId", equalTo(id))
                .body("data.newsById.authorId", equalTo(author))
                .body("data.newsById.text", equalTo(text))
                .body("data.newsById.country", equalTo(country))


        //now, lets delete it
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
                .queryParam("query", "{newsById(id:\"$id\"){newsId, authorId, text, country}}")
                .get()
                .then()
                .statusCode(200)
                .body("data.newsById", equalTo(null))
    }

    //TODO test filters
}