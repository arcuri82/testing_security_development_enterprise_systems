package org.tsdes.advanced.rest.guiv2

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.tsdes.advanced.rest.guiv2.db.Book
import org.tsdes.advanced.rest.guiv2.db.BookRepository
import org.tsdes.advanced.rest.guiv2.dto.BookDto

/**
 * Created by arcuri82 on 14-Sep-18.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(BookApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpaRestBackendApplicationTest {


    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var repository: BookRepository

    @BeforeEach
    @AfterEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/books"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        repository.run {
            deleteAll()
            save(Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", 1979))
            save(Book("The Lord of the Rings", "J. R. R. Tolkien", 1954))
            save(Book("The Last Wish", "Andrzej Sapkowski", 1993))
            save(Book("A Game of Thrones", "George R. R. Martin", 1996))
            save(Book("The Call of Cthulhu", "H. P. Lovecraft", 1928))
        }
    }


    @Test
    fun testGetAll() {

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))
    }

    @Test
    fun testNotFoundBook() {

        given().accept(ContentType.JSON)
                .get("/-3")
                .then()
                .statusCode(404)
    }


    @Test
    fun testRetrieveEachSingleBook() {

        val books = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))
                .extract().body().jsonPath().getList("data", BookDto::class.java)

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

        val n = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .extract().body().path<Int>("data.size()")

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
                .body("data.size()", equalTo(n + 1))
    }


    @Test
    fun testDeleteAllBooks() {

        val books = given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(5))
                .extract().body().jsonPath().getList("data", BookDto::class.java)

        for (b in books) {

            given().accept(ContentType.JSON)
                    .delete("/${b.id}")
                    .then()
                    .statusCode(204)
        }

        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(0))
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