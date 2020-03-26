package org.tsdes.intro.spring.security.manual.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.misc.testutils.HttpUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by arcuri82 on 08-Feb-18.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HttpTest {

    @LocalServerPort
    private int port;

    @Test
    public void testSetCookies() throws Exception {

        String httpGet = "GET / HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "\r\n";
        String response = HttpUtils.executeHttpCommand("localhost", port, httpGet);

        assertTrue(response.contains("200"));
        //SpringBoot creates a new session, and tell us its id to use as cookie in the next HTTP requests
        assertTrue(response.contains("Set-Cookie"));

        System.out.println(response);
    }


    @Test
    public void testCookieHandling() throws Exception{

        String httpGet = "GET / HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "\r\n";

        //do one request with no cookie: we ll get asked by server to set one
        String a = HttpUtils.executeHttpCommand("localhost", port, httpGet);
        assertTrue(a.contains("200"));
        assertTrue(a.contains("Set-Cookie"));

        //another request without cookie: server creates another session,
        //because it thinks it is a different user
        String b = HttpUtils.executeHttpCommand("localhost", port, httpGet);
        assertTrue(b.contains("200"));
        assertTrue(b.contains("Set-Cookie"));

        String first = HttpUtils.getSessionCookie(a);
        String second = HttpUtils.getSessionCookie(b);
        //the two cookies are different, as server creates a session for each HTTP request without
        //the right cookie
        assertNotEquals(first, second);

        //now let's do a request with one of the cookies we got previously
        String httpWithCookie = "GET / HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "Cookie:"+first+"\r\n" +
                "\r\n";

        String c = HttpUtils.executeHttpCommand("localhost", port, httpWithCookie);
        assertTrue(c.contains("200"));
        //as we did set the cookie for JSESSIONID, server should not ask to set a new one
        assertFalse(c.contains("Set-Cookie"));

        System.out.println("COOKIE = " + first+"\n\n");
        System.out.println(c);
    }
}
