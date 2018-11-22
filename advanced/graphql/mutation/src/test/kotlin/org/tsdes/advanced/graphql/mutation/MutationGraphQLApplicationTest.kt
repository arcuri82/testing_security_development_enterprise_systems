package org.tsdes.advanced.graphql.mutation

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MutationGraphQLApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MutationGraphQLApplicationTest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var repository: UserRepository


    @BeforeEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/graphql"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        repository.clear()
    }


    @Test
    fun testEmpty() {

        given().accept(ContentType.JSON)
                .queryParam("query", "{all{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.all.size()", equalTo(0))
    }


    @Test
    fun testNotFound(){
        given().accept(ContentType.JSON)
                .queryParam("query", """
                        {findById(id:"-1"){id, name, surname, age}}"
                    """.trimIndent())
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.findById", equalTo(null))
    }

    @Test
    fun testCreateUser() {

        val name = "Foo"

        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{create(name:\"$name\", surname:\"Bar\", age: 18)}" }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.create", notNullValue())
                .extract().body().path<String>("data.create")

        given().accept(ContentType.JSON)
                .queryParam("query", "{all{id}}")
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.all.size()", equalTo(1))

        given().accept(ContentType.JSON)
                .queryParam("query", """
                        {findById(id:"$id"){id, name, surname, age}}"
                    """.trimIndent())
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.findById.name", equalTo(name))
    }


    @Test
    fun wrongMethodForMutation() {

        val query = """
                    mutation{create(name:\"Foo\", surname:\"Bar\", age: 18)}
                    """.trimIndent()

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("query", query)
                .body("""
                    { "query" : "$query" }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                //works fine
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))

        given().accept(ContentType.JSON)
                .queryParam("query", query.replace('\\',' '))
                .get()
                .then()
                .statusCode(200)
                //fails
                .body("data", equalTo(null))
                .body("$", hasKey("errors"))
    }

    @Test
    fun testUpdate(){

        val name = "Foo"
        val changedName = "Changed"

        val id = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" : "mutation{create(name:\"$name\", surname:\"Bar\", age: 18)}" }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.create", notNullValue())
                .extract().body().path<String>("data.create")


        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{update(user:{id:\"$id\",name:\"$changedName\",surname:\"Bar\",age: 18})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.update", equalTo(true))


        given().accept(ContentType.JSON)
                .queryParam("query", """
                        {findById(id:"$id"){id, name, surname, age}}"
                    """.trimIndent())
                .get()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.findById.name", equalTo(changedName))
    }


    @Test
    fun testUpdateNonExistent(){

        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("""
                    { "query" :
                         "mutation{update(user:{id:\"-1\",name:\"Foo\",surname:\"Bar\",age: 18})}"
                    }
                    """.trimIndent())
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("data"))
                .body("$", not(hasKey("errors")))
                .body("data.update", equalTo(false))

    }



}