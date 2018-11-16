package org.tsdes.intro.spring.security.authorization.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.spring.security.authorization.Application;
import org.tsdes.misc.testutils.HttpUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class HttpTest {

    @LocalServerPort
    private int port;

    private static final AtomicInteger counter = new AtomicInteger(0);

    private String getUniqueId(){
        return "foo_HttpTest_" + counter.getAndIncrement();
    }



    @Test
    public void testFailAccessProtectedResource() throws Exception {

        String httpGet = "GET /protected.xhtml HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "\r\n";
        String response = HttpUtils.executeHttpCommand("localhost", port, httpGet);

        assertFalse(response.contains("200"));
        assertFalse(response.contains("Protected resource"));


        assertTrue(response.contains("302"));
        String location = HttpUtils.getHeaderValue(response, "location");
        assertTrue(location.contains("login."));
    }

}
