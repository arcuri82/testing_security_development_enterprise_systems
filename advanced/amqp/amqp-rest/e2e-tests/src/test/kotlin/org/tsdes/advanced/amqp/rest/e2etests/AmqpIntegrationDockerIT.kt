package org.tsdes.advanced.amqp.rest.e2etests

import io.restassured.RestAssured.given
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by arcuri82 on 11-Aug-17.
 */
@Testcontainers
class AmqpIntegrationDockerIT {

    companion object {

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)

        @Container
        @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)
    }

    @Test
    fun test() {

        //these ports are configured in the Docker Compose file
        val senderPort = 9000
        val receiver0 = 9001
        val receiver1 = 9002

        //make sure to wait till the 3 services are up and running
        await().atMost(180, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    given().port(senderPort).get("/actuator/health").then().body("status", equalTo("UP"))
                    given().port(receiver0).get("/actuator/health").then().body("status", equalTo("UP"))
                    given().port(receiver1).get("/actuator/health").then().body("status", equalTo("UP"))
                    true
                }

        /*
            At the beginning it will be 0.
            But written with "n" in case we do debugging and
            start Docker Compose manually without restarting
            it at each test execution.
         */
        val n = given().port(receiver0)
                .get("counter")
                .then()
                .statusCode(200)
                .extract().body().`as`(Int::class.java)

        /*
            Do a POST to Sender, which will broadcast to the other 2 Receiver
         */
        given().port(senderPort).body("foo_0").post("/sender").then().statusCode(204)

        /*
            It might take some times for the messages to go from Sender to RabbitMQ
            and then to a Receiver
         */
        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    given().port(receiver0)
                            .get("counter")
                            .then()
                            .statusCode(200)
                            .body(equalTo("${n + 1}"))
                    true
                }

        /*
            Send another message, and then query the other Receiver.
            As messages are broadcast, each internal counter of the 2 Receivers
            will have the same value.
         */

        given().port(senderPort).body("foo_1").post("/sender").then().statusCode(204)

        await().atMost(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    given().port(receiver1)
                            .get("counter")
                            .then()
                            .statusCode(200)
                            .body(equalTo("${n + 2}"))
                    true
                }
    }
}