package org.tsdes.spring.soap.nasdaqwiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.common.SingleRootFileSource
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.tsdes.spring.soap.nasdaqwiremock.Main.Companion.NASDAQ_ADDRESS
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by arcuri82 on 04-Aug-17.
 */
/*
        Run it once to record actual XML message from the NASDAQ Web Service
     */

fun main(args: Array<String>) {

    try {
        val wiremockPort = 8099
        val server = WireMockServer(
                wireMockConfig().port(wiremockPort).notifier(ConsoleNotifier(true))
        )
        val m = Paths.get("target/wiremock/mappings")
        val f = Paths.get("target/wiremock/files")
        Files.createDirectories(m)
        Files.createDirectories(f)
        val mappings = SingleRootFileSource(m.toFile().absolutePath)
        val files = SingleRootFileSource(m.toFile().absolutePath)
        server.enableRecordMappings(mappings, files)

        server.start()

        System.setProperty("nasdaqAddress", "localhost:$wiremockPort")

        server.stubFor(
                post(urlMatching(".*"))
                        .willReturn(aResponse().proxiedFrom("http://" + NASDAQ_ADDRESS)))

        Main.names

        server.stop()

    }  finally {
        System.exit(0)
    }
}