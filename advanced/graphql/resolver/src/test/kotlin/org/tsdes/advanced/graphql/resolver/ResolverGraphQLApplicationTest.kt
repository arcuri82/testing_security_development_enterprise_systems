package org.tsdes.advanced.graphql.resolver

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.Matchers.hasKey
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ResolverGraphQLApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResolverGraphQLApplicationTest {


    @LocalServerPort
    protected var port = 0


    @BeforeEach
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
                .queryParam("query", "{allPosts{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allPosts.size()", equalTo(3))
                .body("data.allPosts.id", hasItems("0", "1", "2"))
    }

    @Test
    fun testGetWithAuthors() {

        given().accept(ContentType.JSON)
                .queryParam("query", "{allPosts{id,author{name}}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allPosts.size()", equalTo(3))
                .body("data.allPosts.id", hasItems("0", "1", "2"))
                .body("data.allPosts.author.name", hasItems("Foo", "John"))
    }

    @Test
    fun testNestingCyclePostToCommentsToParentPost() {

        given().accept(ContentType.JSON)
                .queryParam("query", "{allPosts{id,author{name},text,comments{id,text,author{name},parentPost{id,text}}}}\n")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.allPosts.size()", equalTo(3))
                .body("data.allPosts.id", hasItems("0", "1", "2"))
                .body("data.allPosts.comments.parentPost.id.flatten()", hasItems("0", "1"))
                .body("data.allPosts.comments.parentPost.id.flatten()", not(hasItems("2")))
    }
}