package org.tsdes.advanced.microservice.discovery.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.util.concurrent.TimeUnit

@Testcontainers
class DiscoveryIntegrationDockerIT {

    companion object {

        @BeforeAll
        @JvmStatic
        fun checkEnvironment(){

            /*
                TODO
                Looks like currently some issues in running Docker-Compose on Travis
             */

//            val travis = System.getProperty("TRAVIS") != null
//            assumeTrue(!travis)
        }

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)


        @Container
        @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)


        @BeforeAll
        @JvmStatic
        fun waitForServers() {

            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

            /*
                Wait for when these services are up and running.
                Status endpoints are added automatically when the Actuator
                library is on the pom.xml dependencies
             */
            Awaitility.await()
                    .atMost(90, TimeUnit.SECONDS)
                    .pollInterval(3, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {
                        given().port(8761).get("/actuator/health").then().body("status", equalTo("UP"))
                        given().port(9000).get("/actuator/health").then().body("status", equalTo("UP"))
                        given().port(9001).get("/actuator/health").then().body("status", equalTo("UP"))
                        given().port(9002).get("/actuator/health").then().body("status", equalTo("UP"))
                        given().port(9003).get("/actuator/health").then().body("status", equalTo("UP"))
                        true
                    }

            /*
                Not only we need to wait for the servers to be up and running,
                but also need to wait for when they are registered on Eureka.

                Note: with default settings, when running many Docker images on
                a laptop, might take a while before all services are registered,
                especially considering that heartbeats might be sent just every
                30s, and there is the need of up to 3 heartbeats to complete
                all registrations.
                Plus all caches need to be updated.
                Long story short: it can take up to 2 minutes to have all system
                up and running:
                https://github.com/spring-cloud/spring-cloud-netflix/issues/373
            */

            Awaitility.await()
                    .atMost(120, TimeUnit.SECONDS)
                    .pollInterval(6, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {
                        given().port(8761).get("/eureka/apps")
                                .then()
                                .body("applications.application.instance.size()", equalTo(4))
                        true
                    }

            /*
                Might take time before the list of available instances per service
                is updated in all the clients of Eureka for client-side balancing
                with Ribbon.
                However, although here we can check that Consumer can speak with at
                least 1 Producer, it does not necessarily mean that it has yet the
                IP addresses of all 3 producers.
            */
            Awaitility.await()
                    .atMost(40, TimeUnit.SECONDS)
                    .pollInterval(4, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {
                        val msg = callConsumer()
                        assertTrue(msg.startsWith("Received:"), msg)
                        assertTrue(!msg.contains("ERROR", ignoreCase = true), msg)
                        true
                    }
        }

        private fun callConsumer(): String{

            return given().accept(ContentType.TEXT)
                    .get("http://localhost:9000/consumerData")
                    .then()
                    .statusCode(200)
                    .extract().body().asString()
        }
    }


    @Test
    fun testIntegration(){

        /*
            Now that everything is (mostly) setup, send many messages.
            Each "Producer" service should get at least 1 of them.
            "Mostly" -> Consumer might have to wait up to 30s to get
            notified by Eureka of all Producers
         */

        val responses : MutableList<String> = mutableListOf()

        var counter = 0

        Awaitility.await()
                .atMost(40, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    (0 until 10).forEach {
                        counter++
                        responses.add(callConsumer())
                    }

                    assertEquals(counter, responses.size)

                    //should get responses from all 3 Producers
                    assertEquals(3, responses.toSet().size)

                    true
                }

    }
}