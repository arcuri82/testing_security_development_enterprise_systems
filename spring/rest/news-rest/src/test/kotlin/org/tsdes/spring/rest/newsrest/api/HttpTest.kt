package org.tsdes.spring.rest.newsrest.api


import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertTrue
import org.junit.Test
import org.tsdes.spring.rest.newsrest.dto.NewsDto
import org.tsdes.spring.utils.HttpUtil

/**
 * Note: these are not really "tests", but rather a simple way to
 * call the server via REST and check results in an automated way:
 * this is done to try out what are the return values of the different
 * HTTP verbs/methods
 */
class HttpTest : NRTestBase() {

    /*
        Note: here I am mixing RestAssured and direct TCP
     */


    private fun createAPost(): String {
        val author = "author"
        val text = "someText"
        val country = "Norway"
        val dto = NewsDto(null, author, text, country, null)

        get().then().statusCode(200).body("size()", equalTo(0))

        val id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        return id
    }

    @Test
    fun testOptions() {

        given().options()
                .then()
                .statusCode(200)
                //Allow: HEAD, POST, GET, OPTIONS
                .header("Allow", containsString("GET"))
                .header("Allow", containsString("POST"))
                //note: having GET, will get HEAD by default, ie do not need to implement it
                .header("Allow", containsString("HEAD"))
                // this should always be available by default, ie no need to activate/implement it.
                // However, some servers (eg Tomcat vs Wildfly) might not specify it in the Allow header
                //.header("Allow", containsString("OPTIONS"))
                //
                .header("Allow", not<String>(containsString("DELETE")))
    }

    @Test
    fun testMissingVerb() {

        delete().then()
                .statusCode(405) //method not allowed
                .header("Allow", containsString("GET")) //response should tell what is allowed
                .header("Allow", containsString("POST"))
        // HEAD and OPTIONS should be allowed as well, but server might fail to send such info.
        // This behavior is not consistent among servers.
        //.header("Allow", containsString("HEAD"))
        //.header("Allow", containsString("OPTIONS"))
    }

    @Test
    fun testWrongVerb() {

        /*
            THIS IS VERY IMPORTANT

            the rfc7231 states:

            "The 405 (Method Not Allowed) status code indicates that the method
             received in the request-line is known by the origin server but not
             supported by the target resource."

            "The 501 (Not Implemented) status code indicates that the server does
             not support the functionality required to fulfill the request. This
             is the appropriate response when the server does not recognize the
             request method"

             So if I make up a fake verb, eg FOO, I would expect 501.
             However, many servers return 405.
             Why is this very important?
             Because you always have to keep in mind that the frameworks you are using
             can have BUGS!!!

             Is this a critical bug? Answer is NO.
             Actually, I do prefer what this server is doing here, ie returning
             a 4xx error (user-error) instead of 5xx (server-error).

             I would go as far as to say that the "bug" is in the specs, ie
             501 should really be in the 4xx family
         */

        var http = "FOO /newsrest/api/news HTTP/1.1\r\n"
        http += "Host:localhost:$port\r\n"
        http += "\n"

        val response = HttpUtil.executeHttpCommand("localhost", port, http)
        assertTrue("Response:\n" + response, response.contains("405"))
    }

    @Test
    fun testHead() {

        val id = createAPost()

        var getRequest = "GET /newsrest/api/news/id/$id HTTP/1.1\r\n"
        getRequest += "Host:localhost:$port\r\n"
        getRequest += "Accept:application/json\r\n"
        getRequest += "\r\n"

        val resultOfGet = HttpUtil.executeHttpCommand("localhost", port, getRequest)

        val headersOfGet = HttpUtil.getHeaderBlock(resultOfGet)
        assertTrue(headersOfGet.contains("200"))

        /*
            Note: the server could choose to send the data in different ways, for example
            - directly, by specifying in "Content-length" header the size in bytes of the payload
            - in "chunks", by specifying "Transfer-Encoding: chunked". This is usually
              useful when the payload is large and/or its size is not known until the request
              is fully processed (so the intermediary chunks computed so far can be sent to client while the
              server is still completing to compute the full response)
         */

        assertTrue(headersOfGet.contains("Transfer-Encoding") || headersOfGet.contains("Content-length"))

        val dataOfGet = HttpUtil.getBodyBlock(resultOfGet)
        assertTrue(dataOfGet != null && !dataOfGet.isBlank()) //check there is a body content

        //now do HEAD
        val headRequest = getRequest.replace("GET", "HEAD")
        val resultOfHead = HttpUtil.executeHttpCommand("localhost", port, headRequest)

        val headersOfHead = HttpUtil.getHeaderBlock(resultOfHead)
        assertTrue(headersOfHead.contains("200"))

        //should get the same headers with info about the payload
        assertTrue(headersOfHead.contains("Transfer-Encoding") || headersOfGet.contains("Content-length"))

        //... but no body content should had been sent
        val dataOfHead = HttpUtil.getBodyBlock(resultOfHead)
        assertTrue(dataOfHead == null || dataOfHead.isEmpty())
    }


    /*
        Note: there are also other 2 verbs:

        TRACE: for debugging when you a complex chain of proxies, eg to check what modifications
               they do on the messages. It is off by default.

        CONNECT: for some types tunneling in proxy servers


        There is also another (important) verb called PATCH covered by rfc5789,
        which is used to do partial updates by sending a set of "instructions"
        on how to do such updates (which might not be idempotent)
     */
}