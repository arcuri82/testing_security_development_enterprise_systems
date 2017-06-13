package org.tsdes.jee.jsf.examples.ex04;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/*
    Note: as we need Wildfly up and running to execute this test, we mark this test as
    an integration one (ie *IT.class).
 */
public class CookieIT {

    /*
        This code does an HTTP call via TCP toward the given host (Wildfly in our case),
        and read the response as a string.

        Note: in production, you would not do something like this, as this is very low level TCP handling.
        It would be better to use a library that extract away such low level details, eg
        like Apache HttpClient.
     */
    private String executeHttpCommand(String host, int port, String request) throws Exception {

        try (Socket socket = new Socket(host, port)) {
            socket.getOutputStream().write(request.getBytes());
            socket.shutdownOutput();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String response = "";
            String line = in.readLine();

            while (line != null) {
                response += line + "\n";
                line = in.readLine();
            }
            return response;
        }
    }

    /*
        As we are dealing with HTTP responses as raw strings, we need some parsing
        to extract the header data we need (JSESSIONID in the "Set-Cookie" header in
        this particular case)
     */
    private String extractJSessionId(String response) {
        Scanner scanner = new Scanner(response);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("Set-Cookie")) {
                return Arrays.asList(line.split(":")[1].split(";"))
                        .stream()
                        .filter(s -> s.contains("JSESSIONID"))
                        .findAny()
                        .get();

            }
        }
        return null;
    }

    @Test
    public void testSetCookies() throws Exception {

        String httpGet = "GET /examples/ex04/ex04.jsf  HTTP/1.1\nHost:localhost\n\n";
        String response = executeHttpCommand("localhost", 8080, httpGet);

        assertTrue(response.contains("200 OK"));
        //Wildfly creates a new session, and tell us its id to use as cookie in the next HTTP requests
        assertTrue(response.contains("Set-Cookie"));

        System.out.println(response);
    }

    @Test
    public void testCookiesHandling() throws Exception{

        String httpGet = "GET /examples/ex04/ex04.jsf  HTTP/1.1\nHost:localhost\n\n";

        //do one request with no cookie: we ll get asked by Wildfly to set one
        String a = executeHttpCommand("localhost", 8080, httpGet);
        assertTrue(a.contains("200 OK"));
        assertTrue(a.contains("Set-Cookie"));

        //another request without cookie: Wildfly creates another session
        String b = executeHttpCommand("localhost", 8080, httpGet);
        assertTrue(b.contains("200 OK"));
        assertTrue(b.contains("Set-Cookie"));

        String first = extractJSessionId(a);
        String second = extractJSessionId(b);
        //the two cookies are different, as Wildfly creates a session for each HTTP request without
        // the right cookie
        assertNotEquals(first, second);

        //now let's do a request with one of the cookies we got previously
        String httpWithCookie = "GET /examples/ex04/ex04.jsf  HTTP/1.1\nHost:localhost\n";
        httpWithCookie+="Cookie:"+first+"\n\n";

        String c = executeHttpCommand("localhost", 8080, httpWithCookie);
        assertTrue(c.contains("200 OK"));
        //as we did set the cookie for JSESSIONID, Wildfly should not ask to set a new one
        assertFalse(c.contains("Set-Cookie"));

        System.out.println("COOKIE = " + first+"\n\n");
        System.out.println(c);
    }


}
