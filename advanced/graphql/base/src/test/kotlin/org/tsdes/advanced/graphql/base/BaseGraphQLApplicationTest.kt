package org.tsdes.advanced.graphql.base

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.hasKey
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.misc.testutils.HttpUtils


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BaseGraphQLApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseGraphQLApplicationTest {

    @LocalServerPort
    protected var port = 0


    @Before
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/graphql"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }


    @Test
    fun testBaseGet() {

        given().accept(ContentType.JSON)
                .queryParam("query", "{all{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("error")))
                .body("data.all.size()", equalTo(4))
                .body("data.all.id", hasItems("0", "1", "2", "3"))
                .body("data.all[0]", hasKey("id"))
                //shouldn't get the other fields
                .body("data.all[0]", not(hasKey("name")))
                .body("data.all[0]", not(hasKey("surname")))
                .body("data.all[0]", not(hasKey("age")))

    }

    @Test
    fun testIdAndName() {

        given().accept(ContentType.JSON)
                .queryParam("query", "{all{id,name}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("error")))
                .body("data.all.size()", equalTo(4))
                .body("data.all.id", hasItems("0", "1", "2", "3"))
                .body("data.all[0]", hasKey("id"))
                //now name should be included
                .body("data.all.name", hasItems("Foo", "Joe", "John", "Mario"))
                .body("data.all[0]", hasKey("name"))
                //but still not the other fields
                .body("data.all[0]", not(hasKey("surname")))
                .body("data.all[0]", not(hasKey("age")))
    }


    @Test
    fun testBasePost() {

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "{all{id}}" }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("error")))
                .body("data.all.size()", equalTo(4))
                .body("data.all.id", hasItems("0", "1", "2", "3"))
                .body("data.all[0]", hasKey("id"))
                //shouldn't get the other fields
                .body("data.all[0]", not(hasKey("name")))
                .body("data.all[0]", not(hasKey("surname")))
                .body("data.all[0]", not(hasKey("age")))

    }


    @Test
    fun testRawGet() {

        /*
            Recall: query parameters have to be escaped, eg to avoid
            conflicts with control symbols like ? and &
            %7B = {
            %7d = }
         */

        val escapedQuery = "%7Ball%7Bname%7D%7D"

        var getRequest = "GET /graphql?query=$escapedQuery HTTP/1.1\r\n"
        getRequest += "Host:localhost:$port\r\n"
        getRequest += "Accept:application/json\r\n"
        getRequest += "\r\n"

        val result = HttpUtils.executeHttpCommand("localhost", port, getRequest)

        val headers = HttpUtils.getHeaderBlock(result)
        assertTrue(headers.contains("200"))

        val body = HttpUtils.getBodyBlock(result)
        body.contains("Foo")
    }


    @Test
    fun testRawPost() {

        val charset = "UTF-8"
        val json = """
                    { "query" : "{all{id}}" }
                    """.trimIndent()
        val size = json.toByteArray(charset(charset)).size

        var postRequest = "POST /graphql HTTP/1.1\r\n"
        postRequest += "Host:localhost:$port\r\n"
        postRequest += "Accept:application/json\r\n"
        postRequest += "Content-Type:application/json; charset=$charset\r\n"
        postRequest += "Content-Length:$size\r\n"
        postRequest += "\r\n"
        postRequest += json

        val result = HttpUtils.executeHttpCommand("localhost", port, postRequest)

        val headers = HttpUtils.getHeaderBlock(result)
        assertTrue(headers.contains("200"))

        val body = HttpUtils.getBodyBlock(result)
        body.contains("Foo")
    }
}