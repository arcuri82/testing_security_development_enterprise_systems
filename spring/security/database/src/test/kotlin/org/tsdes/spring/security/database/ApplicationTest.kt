package org.tsdes.spring.security.database

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.authentication.FormAuthConfig
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import org.tsdes.spring.security.database.db.UserRepository

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest{

    @Autowired
    private lateinit var userRepository: UserRepository

    @LocalServerPort
    private var port = 0


    @Before
    fun initialize() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Before
    fun clean(){
        userRepository.deleteAll()
    }


    @Ignore //FIXME
    @Test
    fun testUnauthorizedAccess(){

        given().accept(ContentType.TEXT)
                .get("/resource")
                .then()
                .statusCode(401)
    }

    @Test
    fun testFailedLogin(){

        given().auth().form("nonRegisteredUser","password",
                FormAuthConfig("login", "the_user", "the_password"))
                .post("/login")
                .then()
                .statusCode(302)
                .header("location", containsString("/login?error"))
    }

    @Test
    fun testRegisterUser(){

        given().contentType(ContentType.URLENC)
                .formParam("the_user", "foo")
                .formParam("the_password", "bar")
                .post("/signIn")
                .then()
                .statusCode(204)
    }

    @Test
    fun testFailDoubleRegistration(){

        val name = "foo"
        val pwd = "bar"

        given().contentType(ContentType.URLENC)
                .formParam("the_user", name)
                .formParam("the_password", pwd)
                .post("/signIn")
                .then()
                .statusCode(204)

        given().contentType(ContentType.URLENC)
                .formParam("the_user", name)
                .formParam("the_password", pwd)
                .post("/signIn")
                .then()
                .statusCode(400)
    }


    @Test
    fun testRegisterAndThenLogin(){

        val name = "foo"
        val pwd = "bar"

        given().contentType(ContentType.URLENC)
                .formParam("the_user", name)
                .formParam("the_password", pwd)
                .post("/signIn")
                .then()
                .statusCode(204)


        given().contentType(ContentType.URLENC)
                .formParam("the_user", name)
                .formParam("the_password", pwd)
                .post("/login")
                .then()
                .statusCode(302)
                .header("location", not(containsString("/login?error")))
    }
}