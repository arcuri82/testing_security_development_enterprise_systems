package org.tsdes.advanced.rest.guiv2

import io.restassured.common.mapper.TypeRef
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.advanced.rest.guiv2.db.BookRepository
import org.tsdes.advanced.rest.guiv2.db.DatabaseInitializer
import org.tsdes.advanced.rest.guiv2.dto.BookDto

/**
 * Created by arcuri82 on 14-Sep-18.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(BookApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpaRestBackendApplicationTest {


    @LocalServerPort protected var port = 0

    @Autowired protected lateinit var repository: BookRepository

    @Autowired protected lateinit var dbInitializer: DatabaseInitializer

    val page : Int = 5

    @BeforeEach
    @AfterEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/books"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        dbInitializer.initialize()
    }


    @Test
    fun testGetPage() {

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(page))
    }


    @Test
    fun testAllPages(){

        RestAssured.basePath = ""
        val read = mutableSetOf<String>()

        var page = given().accept(ContentType.JSON)
                .get("/api/books")
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(page))
                //unfortunately, RestAssured does not have good support for Generics... :(
                .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String,Object>>>(){})
        read.addAll(page.list.map { it["id"].toString()})

        checkOrder(page)

        while(page.next != null){
            page = given().accept(ContentType.JSON)
                    .get(page.next)
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String,Object>>>(){})
            read.addAll(page.list.map { it["id"].toString()})
            checkOrder(page)
        }

        val total = repository.count().toInt()

        //recall that sets have unique elements
        assertEquals(total, read.size)
    }

    private fun checkOrder(page: PageDto<Map<String, Object>>) {
        for (i in 0 until page.list.size - 1) {
            val ayear = page.list[i]["year"].toString().toInt()
            val byear = page.list[i + 1]["year"].toString().toInt()
            val aid = page.list[i]["id"].toString().toLong()
            val bid = page.list[i + 1]["id"].toString().toLong()
            assertTrue(ayear >= byear)
            if (ayear == byear) {
                assertTrue(aid > bid)
            }
        }
    }

    @Test
    fun testNotFoundBook() {

        given().accept(ContentType.JSON)
                .get("/-3")
                .then()
                .statusCode(404)
    }


    @Test
    fun testRetrieveEachSingleBookInPage() {

        val books = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(page))
                .extract().body().jsonPath().getList("data.list", BookDto::class.java)

        for (b in books) {

            given().accept(ContentType.JSON)
                    .get("/${b.id}")
                    .then()
                    .statusCode(200)
                    .body("data.title", equalTo(b.title))
                    .body("data.author", equalTo(b.author))
                    .body("data.year", equalTo(b.year))
        }
    }


    @Test
    fun testCreateBook() {

        repository.deleteAll()

        val n = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract().body().path<Int>("data.list.size()")

        assertEquals(0, n)

        val title = "foo"

        val location = given().contentType(ContentType.JSON)
                .body(BookDto(title, "bar", 123))
                .post()
                .then()
                .statusCode(201)
                .extract().header("location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("data.title", equalTo(title))

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(n + 1))
    }


    @Test
    fun testBooks() {

        val books = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(page))
                .extract().body().jsonPath().getList("data.list", BookDto::class.java)

        for (b in books) {

            given().accept(ContentType.JSON)
                    .get("/${b.id}")
                    .then()
                    .statusCode(200)

            given().accept(ContentType.JSON)
                    .delete("/${b.id}")
                    .then()
                    .statusCode(204)

            given().accept(ContentType.JSON)
                    .get("/${b.id}")
                    .then()
                    .statusCode(404)
        }
    }


    @Test
    fun testUpdateBook() {

        val title = "foo"

        val location = given().contentType(ContentType.JSON)
                .body(BookDto(title, "bar", 123))
                .post()
                .then()
                .statusCode(201)
                .extract().header("location")

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("data.title", equalTo(title))

        val id = location.substring(location.lastIndexOf('/') + 1)

        val modified = "modified"

        given().contentType(ContentType.JSON)
                .body(BookDto(modified, "bar", 123, id))
                .put("/$id")
                .then()
                .statusCode(204)

        given().accept(ContentType.JSON)
                .basePath("")
                .get(location)
                .then()
                .statusCode(200)
                .body("data.title", equalTo(modified))
    }
}