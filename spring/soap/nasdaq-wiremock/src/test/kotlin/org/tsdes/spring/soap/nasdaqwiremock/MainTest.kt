package org.tsdes.spring.soap.nasdaqwiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


/**
 * Created by arcuri82 on 04-Aug-17.
 */
class MainTest {

    private val wiremockPort = 8099

    private val server: WireMockServer = WireMockServer(
            wireMockConfig().port(wiremockPort).notifier(ConsoleNotifier(true))
    )

    @Before
    fun initWiremock() {

        server.start()

        val body = MainTest::class.java.getResource("/body-v1-NASDAQQuotes.asmx.xml").readText()

        server.stubFor(
                post(urlMatching("/v1/NASDAQQuotes.asmx"))
                        .withRequestBody(matching(".*ListMarketCenters.*"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "text/xml;charset=utf-8")
                                .withHeader("Content-Length", "${body.length}")
                                .withBody(body)
                        ))
    }

    @After
    fun tearDown() {
        server.stop()
    }


    @Test
    fun test() {
        System.setProperty("nasdaqAddress", "localhost:$wiremockPort")

        assertTrue(Main.names!!.contains("The NASDAQ Stock Market LLC"))
        assertTrue(Main.names!!.contains("A Mocked Name"))
    }

}