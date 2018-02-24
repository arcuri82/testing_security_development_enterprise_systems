package org.tsdes.spring.amqp.rest.e2etests

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assume.assumeTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

/**
 * Created by arcuri82 on 11-Aug-17.
 */
@Ignore //FIXME
class AmqpIntegrationDockerIT {

    companion object {

        @BeforeClass @JvmStatic
        fun checkEnvironment(){

            /*
                TODO
                Looks like currently some issues in running Docker-Compose on Travis
             */

            val travis = System.getProperty("TRAVIS") != null
            assumeTrue(! travis)
        }


        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)

        @ClassRule @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
                .withLocalCompose(true)
    }


    /*
        TODO: GenericContainer has a waitingFor method, but not DockerComposeContainer.
        Code below can be replace once following feature is released:

        https://github.com/testcontainers/testcontainers-java/issues/174
     */


    private fun waitForSpring(port: Int, timeoutMS: Long) {

        /*
            When we start a Docker image, it might take some seconds for
            a Spring application to boot.
            We need to wait until the Spring application is ready to handle
            REST calls.
            Note: this is a particular problem when you run many Docker
            images (eg with Compose) on a single development laptop, as
            CPUs and memory are limited.
         */

        assertWithinTime(timeoutMS, {
            /*
                Note: to automatically add the /health endpoint (and others) in
                a Spring application, need to have Actuator dependency on the
                classpath (eg, "spring-boot-starter-actuator").
                If Spring Boot sees such dependency on the classpath, it will
                automatically configure it.
             */
            given().port(port).get("/health").then().body("status", equalTo("UP"))
        })
    }

    private fun assertWithinTime(timeoutMS: Long, lambda: () -> Any) {
        val start = System.currentTimeMillis()

        var delta = 0L

        while (delta < timeoutMS) {
            try {
                lambda.invoke()
                return
            } catch (e: AssertionError) {
                Thread.sleep(100)
                delta = System.currentTimeMillis() - start
            } catch (e: Exception) {
                Thread.sleep(100)
                delta = System.currentTimeMillis() - start
            }
        }

        lambda.invoke()
    }


    @Test
    fun test() {

        //these ports are configured in the Docker Compose file
        val senderPort = 9000
        val receiver0 = 9001
        val receiver1 = 9002

        //make sure to wait till the 3 services are up and running
        waitForSpring(senderPort, 30_000)
        waitForSpring(receiver0, 2_000)
        waitForSpring(receiver1, 2_000)

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
        assertWithinTime(3000, {
            given().port(receiver0)
                    .get("counter")
                    .then()
                    .statusCode(200)
                    .body(equalTo("${n+1}"))
        })

        /*
            Send another message, and then query the other Receiver.
            As messages are broadcast, each internal counter of the 2 Receivers
            will have the same value.
         */

        given().port(senderPort).body("foo_1").post("/sender").then().statusCode(204)

        assertWithinTime(3000, {
            given().port(receiver1)
                    .get("counter")
                    .then()
                    .statusCode(200)
                    .body(equalTo("${n+2}"))
        })
    }
}