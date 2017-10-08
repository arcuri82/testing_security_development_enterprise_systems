package org.tsdes.spring.amqp.rest.e2etests

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

/**
 * Created by arcuri82 on 11-Aug-17.
 */
class IntegrationDockerIT {

//    companion object {
//
//        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)
//
//        @ClassRule @JvmField
//        val env = KDockerComposeContainer(File("../docker-compose.yml"))
//                .withLocalCompose(true)
//    }

    /*
        TODO: GenericContainer has a waitingFor method, but not DockerComposeContainer.
        Code below can be replace once following feature is released:

        https://github.com/testcontainers/testcontainers-java/issues/174
     */


    private fun waitForSpring(port: Int, timeoutMS: Long) {
        assertWithinTime(timeoutMS, {
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
            }
        }

        lambda.invoke()
    }

    /*
        TODO

        This will be fixed in next release of TestContainers, see

        https://github.com/testcontainers/testcontainers-java/issues/439#issuecomment-321812314

        For now, can be run manually with by first running

        docker-compose build
        docker-compose up
     */
    @Ignore
    @Test
    fun test() {

        val senderPort = 9000
        val receiver0 = 9001
        val receiver1 = 9002

        waitForSpring(senderPort, 30_000)
        waitForSpring(receiver0, 2_000)
        waitForSpring(receiver1, 2_000)

        val n = given().port(receiver0)
                .get("counter")
                .then()
                .statusCode(200)
                .extract().body().`as`(Int::class.java)


        given().port(senderPort).body("foo_0").post("/sender").then().statusCode(204)

        assertWithinTime(3000, {
            given().port(receiver0)
                    .get("counter")
                    .then()
                    .statusCode(200)
                    .body(equalTo("${n+1}"))
        })

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