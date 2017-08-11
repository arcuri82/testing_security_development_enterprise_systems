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

    companion object {

        class KDockerComposeContainer(path: File) : DockerComposeContainer<KDockerComposeContainer>(path)

        @ClassRule @JvmField
        val env = KDockerComposeContainer(File("../docker-compose.yml"))
    }

    //TODO wait for init

    @Ignore
    @Test
    fun test() {

        val senderPort = 9000
        val receiver0 = 9001
        val receiver1 = 9002

        given().port(receiver0)
                .get("counter")
                .then()
                .statusCode(200)
                .body(equalTo("0"))


        given().port(senderPort).body("foo_0").post("/sender").then().statusCode(204)

        given().port(receiver0)
                .get("counter")
                .then()
                .statusCode(200)
                .body(equalTo("1"))

        given().port(senderPort).body("foo_1").post("/sender").then().statusCode(204)

        given().port(receiver1)
                .get("counter")
                .then()
                .statusCode(200)
                .body(equalTo("2"))
    }

}