package org.tsdes.spring.soap.nasdaqwiremock

import org.tsdes.spring.soap.nasdaq.client.NASDAQQuotes
import org.tsdes.spring.soap.nasdaq.client.NASDAQQuotesSoap
import javax.xml.ws.BindingProvider

/**
 * Created by arcuri82 on 04-Aug-17.
 */
class Main {

    companion object {

        val NASDAQ_ADDRESS = "ws.nasdaqdod.com"

        var names: List<String>? = null
            private set
            get() {
                if (field == null) {
                    val soap = getNASDAQQuotesSoap()
                    field = soap.listMarketCenters()
                            .getMarketCenter()
                            .map { it.name }
                }
                return field
            }

        private fun getNASDAQQuotesSoap(): NASDAQQuotesSoap {
            //use property, so we can change it during testing to point to WireMock
            val webAddress = System.getProperty("nasdaqAddress", NASDAQ_ADDRESS)

            val service = NASDAQQuotes()
            //"javax.xml.ws.service.endpoint.address" -> "http://ws.nasdaqdod.com/v1/NASDAQQuotes.asmx"
            val url = "http://$webAddress/v1/NASDAQQuotes.asmx"

            val ws = service.getNASDAQQuotesSoap()

            (ws as BindingProvider).requestContext.put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url)

            return ws
        }
    }
}


fun main(args: Array<String>) {
    println(Main.names!!.joinToString("\n"))
}