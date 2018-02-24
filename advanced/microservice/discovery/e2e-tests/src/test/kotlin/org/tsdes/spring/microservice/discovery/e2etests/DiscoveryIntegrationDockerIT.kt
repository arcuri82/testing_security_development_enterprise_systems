package org.tsdes.spring.microservice.discovery.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.Assert.*
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

@Ignore //FIXME
class DiscoveryIntegrationDockerIT {

    companion object {

        @BeforeClass
        @JvmStatic
        fun checkEnvironment(){

            /*
                TODO
                Looks like currently some issues in running Docker-Compose on Travis
             */

            val travis = System.getProperty("TRAVIS") != null
            Assume.assumeTrue(!travis)
        }

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)


        @ClassRule
        @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)
    }

    private fun waitForSpring(port: Int, timeoutMS: Long) {
        assertWithinTime(timeoutMS, {
            RestAssured.given().port(port).get("/health").then().body("status", equalTo("UP"))
        })
    }

    private fun assertWithinTime(timeoutMS: Long, lambda: () -> Any, waitingMS: Long = 100) {
        val start = System.currentTimeMillis()

        var delta = 0L

        while (delta < timeoutMS) {
            delta = try {
                lambda.invoke()
                return
            } catch (e: AssertionError) {
                Thread.sleep(waitingMS)
                System.currentTimeMillis() - start
            } catch (e: Exception) {
                Thread.sleep(waitingMS)
                System.currentTimeMillis() - start
            }
        }

        lambda.invoke()
    }



    @Test
    fun testIntegration(){

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        /*
            Wait for when these services are up and running
         */

        waitForSpring(8761, 60_000)
        waitForSpring(9000, 10_000)
        waitForSpring(9001, 10_000)
        waitForSpring(9002, 10_000)
        waitForSpring(9003, 10_000)

        /*
            Not only we need to wait for the servers to be up and running,
            but also need to wait for when they are registered on Eureka.

            Note: with default settings, when running many Docker images on
            a laptop, might take a while before all services are registered,
            especially considering that heartbeats might be sent just every
            30s, and there is the need of up to 3 heartbeats to complete
            all registrations.
         */

        assertWithinTime(120_000, {
            RestAssured.given().port(8761).get("/eureka/apps")
                    .then()
                    .body("applications.application.instance.size()", equalTo(4))
        }, 1_000)


        /*
            Might take time before the list of available instances per service
            is updated in all the clients of Eureka for client-side balancing
            with Ribbon.
         */
        assertWithinTime(60_000, {
            val msg = callConsumer()
            assertTrue(msg, msg.startsWith("Received:"))
            assertTrue(msg, !msg.contains("ERROR", ignoreCase = true))
        }, 1_000)


        /*
            Now that everything is setup, send many messages.
            Each "Producer" service should get at least 1 of them.
         */

        val responses : MutableList<String> = mutableListOf()

        val n = 100

        (0 until n).forEach { responses.add(callConsumer()) }

        assertEquals(n, responses.size)

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