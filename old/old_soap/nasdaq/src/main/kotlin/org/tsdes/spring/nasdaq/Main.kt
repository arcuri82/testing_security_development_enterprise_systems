package org.tsdes.spring.nasdaq

import org.tsdes.spring.soap.nasdaq.client.NASDAQQuotes

/**
 * Created by arcuri82 on 04-Aug-17.
 */
class Main

fun main(args: Array<String>) {

    //http://www.nasdaqdod.com/NASDAQQuotes.asmx?op=ListMarketCenters

    val service = NASDAQQuotes()
    val ws = service.getNASDAQQuotesSoap()

    for (mc in ws.listMarketCenters().getMarketCenter()) {
        println(mc.name)
    }

    /*
        The NASDAQ Stock Market LLC
        CBOE Stock Exchange
        NASDAQ OMX BX, Inc
        Chicago Stock Exchange
        National Stock Exchange
        International Securities Exchange
        Industry Regulatory Authority
        BATS Exchange Inc.
        NYSE Arca
        NYSE Euronext
        NYSE AMEX
        Market Independent
        EDGA Exchange Inc.
        EDGX Exchange Inc.
        NASDAQ OMX PSX
        BATS Y-Exchange Inc.
        The NASDAQ Stock Market LLC
        OTC Bulletin Board
        OTC Markets Group
         */
}