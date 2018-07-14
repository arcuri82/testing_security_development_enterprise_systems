package org.tsdes.advanced.rest.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.core.IsEqual.equalTo
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by arcuri82 on 03-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test") //to override properties, eg "fixerWebAddress"
class ConverterRestServiceXmlTest {

    companion object {
        /*
            Kotlin has a poor handling of static variables/methods.
            This might change in the future (after 1.1) though, as the
            keyword "static" might be introduced.

            Here we need to start the WireMock HTTP server (eg, Jetty) before
            the tests, and stop it after all tests are completed.
            This is done with @BeforeClass and @AfterClass... but those
            methods have to be static in JUnit 4!!!
            So, here we need to go into a "companion object" and use
            @JvmStatic to get it work... :(

            Note: in the upcoming JUnit 5, this issue can be bypassed,
            because there is no longer requirement on static.

            Plus there is all the issues of accessing injected properties (eg,
            random port for Spring) from a static context...
         */

        private lateinit var wiremockServer: WireMockServer

        @BeforeClass @JvmStatic
        fun initClass() {
            RestAssured.baseURI = "http://localhost"
            RestAssured.port = 8080
            RestAssured.basePath = "/wiremockRest/api/convert"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

            /*
              WireMock will run as a process binding on port 8099 (in this case).
              We configure the application by, during testing, redirecting all
              calls to external services to WireMock instead.
              In WireMock, we need to define mocked answers.
           */
            wiremockServer = WireMockServer(wireMockConfig().port(8099).notifier(ConsoleNotifier(true)))
            wiremockServer.start()
        }

        @AfterClass @JvmStatic
        fun tearDown() {
            wiremockServer.stop()
        }
    }

    private fun getAMockedJsonResponse(usd: Double, eur: Double, gbp: Double): String {
        var json = """
        {
        "base": "NOK" ,
        "date": "2016-04-29",
        "rates": { "USD": $usd,
                   "EUR": $eur,
                   "GBP": $gbp
                 }
        }
        """
        return json
    }

    private fun stubJsonResponse(json: String) {

        /*
            here we are instructing WireMock to return the given json
            every time there is a request for

            /latest?base=NOK

         */

        wiremockServer.stubFor(//prepare a stubbed response for the given request
                WireMock.get(//define the GET request to mock
                        /*
                           recall regular expressions:
                           "." =  any character
                           "*" = zero or more times
                        */
                        urlMatching("/latest.*"))
                        .withQueryParam("base", WireMock.matching("NOK"))
                        // define the mocked response of the GET
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.toByteArray(charset("utf-8")).size)
                                .withBody(json)))
    }

    @Test
    fun testEUR() {

        val eur = 42.0

        val json = getAMockedJsonResponse(1.0, eur, 2.0)
        stubJsonResponse(json)

        given().accept(ContentType.XML)
                .queryParam("from", "NOK")
                .queryParam("to", "EUR")
                .get()
                .then()
                .statusCode(200)
                .body("conversionDTO.rate", equalTo("" + eur))
    }

    @Test
    fun testUSD() {

        val usd = 66.0

        val json = getAMockedJsonResponse(usd, 1.0, 2.0)
        stubJsonResponse(json)

        given().accept(ContentType.XML)
                .queryParam("from", "NOK")
                .queryParam("to", "USD")
                .get()
                .then()
                .statusCode(200)
                .body("conversionDTO.rate", equalTo("" + usd))
    }

    @Test
    fun testGBP() {

        val gbp = 99.0

        val json = getAMockedJsonResponse(1.0, 2.0, gbp)
        stubJsonResponse(json)

        given().accept(ContentType.XML)
                .queryParam("from", "NOK")
                .queryParam("to", "GBP")
                .get()
                .then()
                .statusCode(200)
                .body("conversionDTO.rate", equalTo("" + gbp))
    }

}