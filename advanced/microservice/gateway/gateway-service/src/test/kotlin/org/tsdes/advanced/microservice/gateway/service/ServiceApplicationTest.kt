package org.tsdes.advanced.microservice.gateway.service

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(ServiceApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceApplicationTest{

    @LocalServerPort
    protected var port = 0


    @BeforeEach
    fun setup() {

        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/messages"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        given().delete().then().statusCode(204)
    }


    @Test
    fun testCreate(){

        given().get()
                .then()
                .statusCode(200)
                .body("size", equalTo(0))

        given().contentType(ContentType.TEXT)
                .body("bar")
                .post()
                .then()
                .statusCode(201)

        given().get()
                .then()
                .statusCode(200)
                .body("size", equalTo(1))
                .body("[0].system", equalTo("Undefined"))
                .body("[0].message", equalTo("bar"))
    }

    @Test
    fun testAgain(){
        //just make sure that we have code that clean the database
        testCreate()
    }
}