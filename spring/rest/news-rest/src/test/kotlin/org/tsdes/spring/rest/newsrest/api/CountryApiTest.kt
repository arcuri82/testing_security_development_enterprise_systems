package org.tsdes.spring.rest.newsrest.api

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.spring.rest.newsrest.NewsRestApplication
import org.tsdes.spring.utils.HttpUtil

/**
 * Created by arcuri82 on 12-Jul-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(NewsRestApplication::class),
        webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountryApiTest{

    @LocalServerPort
    private  var port = 0

    @Test
    fun testWithRawTcp() {

        /*
            Here we build a "raw" HTTP request to get the list of countries,
            and do it by using low level TCP connections.
            Note: this is only for didactic purposes
         */

        // "verb" (GET in this case), followed by " ", then the path to resource, " ", and finally the protocol
        var request = "GET /newsrest/api/countries HTTP/1.1\r\n"
        //headers are pairs <key>:<value>, where the key is case insensitive
        request += "Host:localhost:$port\r\n"  //this is compulsory: a server running at an IP can serve different host names
        request += "Accept:application/json\r\n" //we states that we want the resource in Json format
        request += "\r\n" //empty line with CRLF indicates the end of the header section

        val result = HttpUtil.executeHttpCommand("localhost", port, request)
        println(result)

        val headers = HttpUtil.getHeaderBlock(result)
        assertTrue(headers.contains("200"))

        val contentType = HttpUtil.getHeaderValue("Content-Type", result)
        assertTrue(contentType!!.contains("application/json"))

        val body = HttpUtil.getBodyBlock(result)!!
        assertTrue(body.contains("Norway"))
        assertTrue(body.contains("Sweden"))
        assertTrue(body.contains("Germany"))
    }

}