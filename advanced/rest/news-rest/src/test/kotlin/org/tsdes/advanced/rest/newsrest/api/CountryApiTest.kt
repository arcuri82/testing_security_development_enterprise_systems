package org.tsdes.advanced.rest.newsrest.api

import com.google.gson.Gson
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.advanced.rest.newsrest.NewsRestApplication
import org.tsdes.misc.testutils.HttpUtils
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.UriBuilder
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.greaterThan
import org.springframework.web.client.RestTemplate

/**
 * Created by arcuri82 on 12-Jul-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(NewsRestApplication::class)],
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

        val result = HttpUtils.executeHttpCommand("localhost", port, request)
        println(result)

        val headers = HttpUtils.getHeaderBlock(result)
        assertTrue(headers.contains("200"))

        val contentType = HttpUtils.getHeaderValue(result, "Content-Type")
        assertTrue(contentType!!.contains("application/json"))

        val body = HttpUtils.getBodyBlock(result)!!
        assertTrue(body.contains("Norway"))
        assertTrue(body.contains("Sweden"))
        assertTrue(body.contains("Germany"))
    }


    @Test
    fun testWithApacheHttpClient() {

        /*
            There are libraries to simplify the use of HTTP.
            The most popular is Apache HttpClient.
            Note: this is a generic HTTP library, and not specific for REST
         */

        val httpclient = HttpClients.createDefault()

        val httpGet = HttpGet("http://localhost:$port/newsrest/api/countries")
        httpGet.addHeader("Accept", "application/json")

        val response = httpclient.execute(httpGet)
        assertEquals(200, response.statusLine.statusCode.toLong())

        val body = EntityUtils.toString(response.entity)
        println(body)

        val countries = Gson().fromJson(body, List::class.java)

        assertTrue(countries.size > 200)
        assertTrue(countries.contains("Norway"))
        assertTrue(countries.contains("Sweden"))
        assertTrue(countries.contains("Germany"))

        httpGet.releaseConnection()
    }

    @Test
    fun testWithRestEasy() {

        /*
            If you are dealing with REST, you might want to use a library that is
            specific for REST, like RestEasy.
            Note: this is the same library used internally in Wildfly to create
            REST services (ie, an implementation of JAX-RS).
         */

        val uri = UriBuilder.fromUri("http://localhost/newsrest/api/countries")
                .port(port)
                .build()
        val client = ClientBuilder.newClient()

        val response = client.target(uri).request("application/json").get()
        assertEquals(200, response.status.toLong())

        val body = response.readEntity(String::class.java)

        val gson = Gson()
        val countries = gson.fromJson(body, List::class.java)

        assertTrue(countries.size > 200)
        assertTrue(countries.contains("Norway"))
        assertTrue(countries.contains("Sweden"))
        assertTrue(countries.contains("Germany"))
    }


    @Test
    fun testWithSpringRestTemplate(){

        /*
            Not surprisingly, Spring has its own HTTP library to
            work with REST endpoints
         */

        val client =  RestTemplate()
        val response = client.getForEntity("http://localhost:$port/newsrest/api/countries", List::class.java)

        assertEquals(200, response.statusCode.value())

        //note that here the JSON body has already been unmarshalled
        val countries = response.body

        assertTrue(countries.size > 200)
        assertTrue(countries.contains("Norway"))
        assertTrue(countries.contains("Sweden"))
        assertTrue(countries.contains("Germany"))
    }


    @Test
    fun testWithRestAssured() {

        /*
            If your goal is testing REST API, there are libraries that
            are specialized in it. One is RestAssured.
            You can compare the code beneath with the other tests,
            and decide which is the easiest to read.

            One good thing of RestAssured is the ability to define
            assertions on the JSon responses in the body without
            having to manually parsing (or unmarshalling) it first.
         */

        given().accept(ContentType.JSON)
                .and()
                .get("http://localhost:$port/newsrest/api/countries")
                .then()
                .statusCode(200)
                .and()
                .body("size()", greaterThan(200))
                .body(containsString("Norway"))
                .body(containsString("Sweden"))
                .body(containsString("Germany"))
    }
}