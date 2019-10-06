package org.tsdes.advanced.microservice.gateway.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.util.concurrent.TimeUnit

@Testcontainers
abstract class GatewayIntegrationDockerTestBase {

    companion object {

        init {
            /*
               TODO
               Looks like currently some issues in running Docker-Compose on Travis
            */
//
//            val travis = System.getProperty("TRAVIS") != null
//            assumeTrue(!travis)


            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
            RestAssured.port = 80
        }

        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        @Container
        @JvmField
        val env = KDockerComposeContainer("gateway", File("../docker-compose.yml"))
                /*
                    In the docker-compose file, I did not expose the port for Eureka.
                    Nor I made a route for it in the gateway.
                    However, I need it in these tests, as need to query it to wait for it
                    be fully initialized.
                    So, I expose it here.
                 */
                .withExposedService("eureka", 8761)
                .withLocalCompose(true)


        @BeforeAll
        @JvmStatic
        fun waitForServer() {

            /*
                Need to wait for a bit, because it can take up to 2 minutes before
                Eureka is fully initialized and all changes are propagated to all
                of its clients. See:
                https://github.com/Netflix/eureka/wiki/Understanding-eureka-client-server-communication

                Note: here I am using the Awaitility library to do such waits
             */

            await().atMost(180, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until{

                        given().get("/index.html").then().statusCode(200)

                        given().accept("application/json;charset=UTF-8")
                                .get("/service/messages")
                                .then().statusCode(200)

                        given().accept("application/json;charset=UTF-8")
                                .delete("/service/messages")
                                .then().statusCode(204)

                        given().baseUri("http://${env.getServiceHost("eureka_1", 8761)}")
                                .port(env.getServicePort("eureka_1", 8761))
                                .get("/eureka/apps")
                                .then()
                                .body("applications.application.instance.size()", equalTo(4))

                        true
                    }
        }

    }

}