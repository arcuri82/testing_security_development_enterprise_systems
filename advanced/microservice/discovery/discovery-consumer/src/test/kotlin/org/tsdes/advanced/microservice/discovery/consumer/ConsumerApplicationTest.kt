package org.tsdes.advanced.microservice.discovery.consumer

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by arcuri82 on 15-Oct-18.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [(ConsumerApplicationTest.Companion.Initializer::class)])
class ConsumerApplicationTest{

    @LocalServerPort
    private var port = 0

    companion object {

        private lateinit var wiremockServer: WireMockServer

        const val id = "foo"

        @BeforeAll
        @JvmStatic
        fun initClass() {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

            wiremockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort().notifier(ConsoleNotifier(true)))
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

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of("producer-server-address: localhost:${wiremockServer.port()}")
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }

    @Test
    fun testGetId() {

        val msg = given().accept(ContentType.TEXT)
                .get("http://localhost:$port/consumerData")
                .then()
                .statusCode(200)
                .extract().body().asString()

        assertEquals("Received: $id", msg)
    }
}