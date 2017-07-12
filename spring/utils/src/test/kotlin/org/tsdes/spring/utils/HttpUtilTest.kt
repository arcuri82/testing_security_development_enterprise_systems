package org.tsdes.spring.utils

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by arcuri82 on 06-Jul-17.
 */
class HttpUtilTest{

    private val msg = """
            POST /foo HTTP/1.1
            Host: localhost:8080
            Content-Type: text/plain; charset=utf-8
            Content-Length: 6

            Hello!
        """.trimIndent()

    @Test
    fun testBody(){
        assertEquals("Hello!", HttpUtil.getBodyBlock(msg))
    }

    @Test
    fun testHeader(){
        assertEquals("6", HttpUtil.getHeaderValue("Content-Length", msg))
    }

    @Test
    fun testAllHeaders(){
        val headers = HttpUtil.getHeaderBlock(msg)
        assertTrue(headers.contains("POST"))
        assertTrue(headers.contains("Host"))
        assertTrue(headers.contains("6"))
        assertFalse(headers.contains("Hello"))
    }
}
