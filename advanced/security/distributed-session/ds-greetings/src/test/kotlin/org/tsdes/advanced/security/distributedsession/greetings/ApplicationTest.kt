package org.tsdes.advanced.security.distributedsession.greetings

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @LocalServerPort
    private var port = 0


    companion object {

        /*
            As this service relies on "user-service" being up and running,
            we need to mock it with WireMock
         */

        private lateinit var wiremockServer: WireMockServer

        @BeforeAll
        @JvmStatic
        fun initClass() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.basePath = "/api"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

            wiremockServer = WireMockServer(
                    WireMockConfiguration.wireMockConfig()
                            .port(8099).notifier(ConsoleNotifier(true)))
            wiremockServer.start()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }
    }

    @BeforeEach
    fun initialize() {
        RestAssured.port = port
        wiremockServer.resetAll()
    }


    private fun getAMockedJsonResponse(name: String, surname: String, email: String): String {
        return """
        {
            "name": "$name",
            "surname": "$surname",
            "email": "$email"
        }
        """
    }

    private fun stubJsonResponse(json: String, userId: String) {

        /*
            Here, instructing WireMock to give 2 different responses
            based on whether the authentication cookie is  set or not,
            regardless of its content
         */

        wiremockServer.stubFor(
                WireMock.get(
                        WireMock.urlMatching("/usersInfo/$userId"))
                        .withCookie("SESSION", WireMock.matching(".+"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(json)))

        wiremockServer.stubFor(
                WireMock.get(
                        WireMock.urlMatching("/usersInfo/$userId"))
                        .withCookie("SESSION", WireMock.notMatching(".+"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(403)))

    }


    @Test
    fun testNotAuthenticated(){
        given().get()
                .then()
                .statusCode(401)
    }


    @Test
    fun testUserServiceNotReachable() {

        val id = "foo"

        given().auth().basic(id, "123")
                .accept(ContentType.JSON)
                .get(id)
                .then()
                .statusCode(500)
    }

    @Test
    fun testSession() {

        /*
            Note: this is a bug, as we did not handle
            the different status codes, ie 404 in this case
         */

        val name = "John"
        val surname = "Smith"
        val email = "foo@foo.com"
        val id = "foo"

        val res = getAMockedJsonResponse(name,surname,email)

        stubJsonResponse(res,id)

        given().auth().basic(id, "123")
                .accept(ContentType.JSON)
                .get(id)
                .then()
                .statusCode(200)
                .body("email", equalTo(email))
                .body("message", startsWith("Hello"))
                .body("message", containsString(name))
                .body("message", containsString(surname))


        given().auth().basic(id, "123")
                .accept(ContentType.JSON)
                .queryParam("ignoreSession", "true")
                .get(id)
                .then()
                .statusCode(400)
    }
}