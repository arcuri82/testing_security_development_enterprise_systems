package org.tsdes.advanced.exercises.cardgame.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

@Disabled
@Testcontainers
class RestIT {


    companion object {

        init {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
            RestAssured.port = 80
        }

        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        @Container
        @JvmField
        val env = KDockerComposeContainer("card-game", File("../docker-compose.yml"))
                .withExposedService("discovery", 8500,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(240)))
                .withLocalCompose(true)


        @BeforeAll
        @JvmStatic
        fun waitForServers() {

            Awaitility.await().atMost(240, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {

                        //given().get("/index.html").then().statusCode(200)

                        given().baseUri("http://${env.getServiceHost("discovery", 8500)}")
                                .port(env.getServicePort("discovery", 8500))
                                .get("/v1/agent/services")
                                .then()
                                .body("size()", equalTo(4))

                        true
                    }
        }
    }

    @Test
    fun testGetCollection() {
        Awaitility.await().atMost(60, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    given().get("/api/cards/collection_v1_000")
                            .then()
                            .statusCode(200)
                            .body("data.cards.size", greaterThan(10))
                    true
                }
    }

    @Test
    fun testGetPage() {
        Awaitility.await().atMost(60, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {
                    given().accept(ContentType.JSON)
                            .get("/api/scores")
                            .then()
                            .statusCode(200)
                            .body("data.list.size()", greaterThan(0))
                    true
                }
    }

    @Test
    fun testCreateUser() {
        Awaitility.await().atMost(60, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until {

                    val id = "foo_testCreateUser_" + System.currentTimeMillis()

                    given().put("/api/user-collections/$id")
                            .then()
                            .statusCode(401)

                    val password = "123456"

                    val cookie = given().contentType(ContentType.JSON)
                            .body("""
                                {
                                    "userId": "$id",
                                    "password": "$password"
                                }
                            """.trimIndent())
                            .post("/api/auth/signUp")
                            .then()
                            .statusCode(204)
                            .header("Set-Cookie", CoreMatchers.not(equalTo(null)))
                            .extract().cookie("SESSION")

                    given().cookie("SESSION", cookie)
                            .put("/api/user-collections/$id")
                            .then()
                            .statusCode(201)

                    true
                }
    }
}