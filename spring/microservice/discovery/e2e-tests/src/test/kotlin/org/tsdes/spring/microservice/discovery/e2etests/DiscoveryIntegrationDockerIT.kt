package org.tsdes.spring.microservice.discovery.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test


class DiscoveryIntegrationDockerIT {

    private fun waitForSpring(port: Int, timeoutMS: Long) {
        assertWithinTime(timeoutMS, {
            RestAssured.given().port(port).get("/health").then().body("status", equalTo("UP"))
        })
    }

    private fun assertWithinTime(timeoutMS: Long, lambda: () -> Any) {
        val start = System.currentTimeMillis()

        var delta = 0L

        while (delta < timeoutMS) {
            delta = try {
                lambda.invoke()
                return
            } catch (e: AssertionError) {
                Thread.sleep(100)
                System.currentTimeMillis() - start
            } catch (e: Exception) {
                Thread.sleep(100)
                System.currentTimeMillis() - start
            }
        }

        lambda.invoke()
    }


    /*
        TODO Docker-Compose
        docker-compose build
        docker-compose up
     */


    @Ignore
    @Test
    fun testIntegration(){

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        waitForSpring(8761, 30_000)
        waitForSpring(9000, 2_000)
        waitForSpring(9001, 2_000)
        waitForSpring(9002, 2_000)
        waitForSpring(9003, 2_000)

        val responses : MutableList<String> = mutableListOf()

        val first = callConsumer()
        assertTrue(first, first.startsWith("Received:"))
        responses.add(first)

        assertTrue(first, ! first.contains("ERROR", ignoreCase = true))

        val second = callConsumer()
        assertTrue(second, second.startsWith("Received:"))
        responses.add(second)

        assertNotEquals(first, second)

        (0 until 48).forEach { responses.add(callConsumer()) }

        assertEquals(50, responses.size)

        assertEquals(3, responses.toSet().size)
    }


    private fun callConsumer(): String{

        return given().accept(ContentType.TEXT)
                .get("http://localhost:9000/consumerData")
                .then()
                .statusCode(200)
                .extract().body().asString()
    }
}