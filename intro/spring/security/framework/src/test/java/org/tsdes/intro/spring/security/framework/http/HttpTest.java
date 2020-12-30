package org.tsdes.intro.spring.security.framework.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.security.framework.Application;
import org.tsdes.misc.testutils.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class HttpTest {

    @LocalServerPort
    private int port;


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
