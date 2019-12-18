package org.tsdes.advanced.microservice.gateway.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeUnit

/**
 * Created by arcuri82 on 04-Oct-19.
 */
@Disabled
@Testcontainers
class GatewayRestIT : GatewayIntegrationDockerTestBase() {

    companion object {
        init {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

    @BeforeEach
    fun clean(){
        given().delete("/service/messages")
                .then()
                .statusCode(204)

        given().get("/service/messages")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))
    }

    private fun sendMsg(msg: String){

        given().contentType(ContentType.TEXT)
                .body(msg)
                .post("/service/messages")
                .then()
                .statusCode(201)
    }

    @Test
    fun testIntegration() {

        given().get("/service/messages")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0))


        sendMsg("Hail Zuul!!!")
        sendMsg("Just kidding")

        given().get("/service/messages")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2))
    }

    @Test
    fun testLoadBalance() {


        Awaitility.await().atMost(120, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until{
                    (0..3).forEach { sendMsg("foo") }

                    val messages = given().get("/service/messages")
                            .then()
                            .statusCode(200)
                            .extract().body().jsonPath().getList("system", String::class.java)

                    assertEquals(3, messages.toSet().size)
                    assertTrue(messages.contains("A"))
                    assertTrue(messages.contains("B"))
                    assertTrue(messages.contains("C"))

                    true
                }
    }
}