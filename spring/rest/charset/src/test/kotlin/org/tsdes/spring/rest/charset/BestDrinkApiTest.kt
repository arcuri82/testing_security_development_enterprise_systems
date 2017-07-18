package org.tsdes.spring.rest.charset

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.tsdes.spring.utils.HttpUtil
import java.nio.charset.Charset

/**
 * Created by arcuri82 on 18-Jul-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BestDrinkApiTest {

    @LocalServerPort
    private var port = 0

    @Test
    fun testGetTheBest() {

        var message = "GET /charset/drinks/best HTTP/1.1\r\n"
        message += "Host:localhost:$port\r\n"
        message += "Accept:text/plain\r\n"
        message += "\r\n"

        val response = HttpUtil.executeHttpCommand("localhost", port, message)
        val body = HttpUtil.getBodyBlock(response)

        assertTrue("Body: $body", body!!.trim().equals("beer", ignoreCase = true))
    }


    @Test
    fun testGetTheBestInNorwegian() {

        var message = "GET /charset/drinks/best HTTP/1.1\r\n"
        message += "Host:localhost:$port\r\n"
        message += "Accept:text/plain\r\n"
        //for language codes, see http://www.w3schools.com/tags/ref_language_codes.asp
        message += "Accept-Language:no\r\n"
        message += "\r\n"

        val response = HttpUtil.executeHttpCommand("localhost", port, message)
        val body = HttpUtil.getBodyBlock(response)

        assertTrue("Body: $body", body!!.trim().equals("øl", ignoreCase = true))
    }

    /*
        There are 3 mains charsets you need to know about:

        ISO-8859-1   https://en.wikipedia.org/wiki/ISO/IEC_8859-1
        UTF-8        https://en.wikipedia.org/wiki/UTF-8
        UTF-16       https://en.wikipedia.org/wiki/UTF-16


        UTF-8 is what you should always use.

        UTF-16 is what the JVM is using internally when handling String objects, eg "øØåÅæÆ".
        You do not need to care about it, because every-time a string "goes out" (eg save
        to a file or sent in a network connection like TCP), then strings will be
        converted to the chosen charset (typically UTF-8, like this very .kt file you
        are reading, but it also depends on the default of the operating system, eg
        the 7-bit US-ASCII).

        ISO-8859-1 is what will give you headaches, and should be avoided at all cost!!!
        Have you ever seen displayed wrong symbols � instead of the characters "øØåÅæÆ"?
        If yes, then most likely someone screwed up and tried to display a
        ISO-8859-1 string with UTF-8.

        Why the need for different charsets?
        - ISO-8859-1 can represent 191 characters (including "øØåÅæÆ"), and use 1 byte.
        - UTF-8 can handle the whole 1,112,064 characters in Unicode, but it is a
          variable length encoding: most common characters need 1 byte, but others (like
          "øØåÅæÆ") can need 2 or more bytes.

        At worst, a document in UTF-8 could be twice as big as ISO-8859-1 (all of the
        191 characters in ISO-8859-1 takes at most 2 bytes in UTF-8).
        However, UTF-8 will avoid not being able to display some special characters (eg,
        think about a chat/forum in which users might want to write in Japanese or
        Chinese). Often, the overhead of UTF-8 is negligible.
        Furthermore, UTF-8 has much larger market share (eg, 87.7% vs 5.8%, see Wikipedia).
        In many systems/programs, the default is UTF-8.
     */

    @Test
    fun testGetWithDifferentCharset() {

        var message = "GET /charset/drinks/best HTTP/1.1\r\n"
        message += "Host:localhost:$port\r\n"
        message += "Accept:text/plain;charset=ISO-8859-1\r\n"
        message += "Accept-Language:en\r\n"
        message += "\r\n"

        val response = HttpUtil.executeHttpCommand("localhost", port, message, "UTF-8")

        val type = HttpUtil.getHeaderValue("Content-Type", response)
        assertTrue(type, type!!.contains("ISO-8859-1"))

        val body = HttpUtil.getBodyBlock(response)

        //no problem, as "beer", being ASCII, has the same bytes in both UTF-8 and ISO-8859-1
        assertTrue("Body: $body", body!!.trim().equals("beer", ignoreCase = true))
    }

    @Test
    fun testGetCharsetProblemIso() {

        var message = "GET /charset/drinks/best HTTP/1.1\r\n"
        message += "Host:localhost:$port\r\n"
        message += "Accept:text/plain;charset=ISO-8859-1\r\n"
        message += "Accept-Language:no\r\n"
        message += "\r\n"

        val response = HttpUtil.executeHttpCommand("localhost", port, message, "UTF-8")

        val type = HttpUtil.getHeaderValue("Content-Type", response)
        assertTrue(type, type!!.contains("ISO-8859-1"))

        var body = HttpUtil.getBodyBlock(response)
        body = body!!.trim()

        //this now fails, as charset conversion problem
        assertFalse("Body: $body", body.toLowerCase() == "øl")
        assertEquals(2, body.length) // first invalid, but still 2 characters
        println("Read value: " + body)
    }

    @Test
    fun testConversionIso() {

        /*
            In ISO-8859-1 , "Øl" has byte values 216 and 108

            In UTF-8, the value 108 represent the lower case letter "l".
            However, 216 is the start of a multi-byte character,
            ie it is a "leading byte" (11xxxxxx).
            But, the following 108, being ASCII (0xxxxxxx), is not
            a "continuation byte" (10xxxxxx),
            and so the system has no idea how to display such invalid
            byte sequence
         */

        val value = "Øl" // this is in UTF-16

        val asIso = value.toByteArray(charset("ISO-8859-1"))

        assertEquals(2, asIso.size)

        val first: Int = asIso[0].toInt() and 0xFF //Java/Kotlin do not have unsigned bytes
        val second: Int = asIso[1].toInt() and 0xFF

        assertEquals(216, first)
        assertEquals(108, second)

        val inUtf8 = String(asIso, Charset.forName("UTF-8"))
        assertNotEquals(value, inUtf8)
        assertEquals("�l", inUtf8)
    }

    @Test
    fun testGetCharsetProblemUtf8() {

        var message = "GET /charset/drinks/best HTTP/1.1\r\n"
        message += "Host:localhost:$port\r\n"
        message += "Accept:text/plain;charset=UTF-8\r\n"
        message += "Accept-Language:no\r\n"
        message += "\r\n"

        val response = HttpUtil.executeHttpCommand("localhost", port, message, "ISO-8859-1")

        val type = HttpUtil.getHeaderValue("Content-Type", response)
        assertTrue(type, type!!.contains("UTF-8"))

        var body = HttpUtil.getBodyBlock(response)
        body = body!!.trim()

        //this now fails, as charset conversion problem
        assertFalse("Body: $body", body.toLowerCase() == "øl")
        assertEquals(3, body.length) // first invalid, but still 2 characters

        assertEquals("Ã\u0098l", body)
        println("Body: " + body)
    }

    @Test
    fun testConversionUtf8() {

        val value = "Øl" // this is in UTF-16

        val asUtf = value.toByteArray(charset("UTF-8"))

        assertEquals(3, asUtf.size)

        val first = asUtf[0].toInt() and 0xFF
        val second = asUtf[1].toInt() and 0xFF
        val third = asUtf[2].toInt() and 0xFF

        //leading byte for Ø, but in ISO-8859-1 it is Ã
        assertEquals(0b11000011, first)
        assertEquals(195, first)

        //continuation byte for Ø, which in ISO-8859-1 is special
        //control "Start of String" (not an actual character).
        //however, to display it in a Java/Kotlin "" string, as it is UTF-16,
        //I need to use its unicode, which is 0098 (exadecimal), ie \u0098

        assertEquals(0b10011000, second)
        assertEquals(152, second)

        //single byte character (ASCII) as it starts with 0
        assertEquals(0b01101100, third)
        assertEquals(108, third)

        val inIso = String(asUtf, Charset.forName("ISO-8859-1"))
        assertNotEquals(value, inIso)
        assertEquals(3, inIso.length)

        assertEquals("Ã\u0098l", inIso)
    }

}