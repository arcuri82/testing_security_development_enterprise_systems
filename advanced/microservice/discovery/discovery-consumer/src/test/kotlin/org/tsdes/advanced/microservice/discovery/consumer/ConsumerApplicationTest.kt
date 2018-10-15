package org.tsdes.advanced.microservice.discovery.consumer

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by arcuri82 on 15-Oct-18.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConsumerApplicationTest{

    @LocalServerPort
    private var port = 0

    companion object {

        private lateinit var wiremockServer: WireMockServer

        const val id = "foo"

        @BeforeClass
        @JvmStatic
        fun initClass() {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

            wiremockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(8099).notifier(ConsoleNotifier(true)))
            wiremockServer.start()

            wiremockServer.stubFor(//prepare a stubbed response for the given request
                    WireMock.get(//define the GET request to mock
                            WireMock.urlMatching("/producerData.*"))
                            // define the mocked response of the GET
                            .willReturn(WireMock.aResponse()
                                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                                    .withHeader("Content-Length", "" + id.length)
                                    .withBody(id)))
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }
    }

    @Test
    fun testGetId() {

        val msg = given().accept(ContentType.TEXT)
                .get("http://localhost:$port/consumerData")
                .then()
                .statusCode(200)
                .extract().body().asString()

        Assert.assertEquals("Received: $id", msg)
    }
}