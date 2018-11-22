package org.tsdes.misc.testutils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpUtilsTest {


    private static final String msg = "POST /foo HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Content-Type: text/plain; charset=utf-8\n"
            + "Content-Length: 6\n"
            + "\n"
            + "Hello!";



    @Test
    public void testBody(){
        assertEquals("Hello!\n", HttpUtils.getBodyBlock(msg));
    }



    @Test
    public void  testHeader(){
        assertEquals("6", HttpUtils.getHeaderValue(msg, "Content-Length"));
    }


    @Test
    public void  testAllHeaders(){
        String headers = HttpUtils.getHeaderBlock(msg);
        assertTrue(headers.contains("POST"));
        assertTrue(headers.contains("Host"));
        assertTrue(headers.contains("6"));
        assertFalse(headers.contains("Hello"));
    }
}