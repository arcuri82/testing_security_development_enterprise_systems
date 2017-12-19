package org.tsdes.spring.microservice.gateway.service

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ServiceApplication::class),
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceApplicationTest{

    @LocalServerPort
    protected var port = 0


    @Before
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