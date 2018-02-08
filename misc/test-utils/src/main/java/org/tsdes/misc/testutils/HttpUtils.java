package org.tsdes.misc.testutils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class with methods to do low-level HTTP communications using raw TCP sockets.
 * Not something you should use, but just to understand what HTTP communications
 * actually are.
 * <p>
 * Created by arcuri82 on 08-Feb-18.
 */
public class HttpUtils {

    /**
     * Connect to given host:port via direct TCP socket, send the input string, and
     * return the result (if any) as a string
     */
    public static String executeHttpCommand(String host, int port, String request) throws Exception {
        return executeHttpCommand(host, port, request, "UTF-8");
    }

    /**
     * Connect to given host:port via direct TCP socket, send the input string, and
     * return the result (if any) as a string
     */
    public static String executeHttpCommand(String host, int port, String request, String charset) throws Exception {
        Objects.requireNonNull(host);
        Objects.requireNonNull(request);

        try (Socket socket = new Socket(host, port)) {
            socket.getOutputStream().write(request.getBytes(charset));
            socket.shutdownOutput();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), charset));

            StringBuilder response = new StringBuilder();
            String line = in.readLine();

            while (line != null) {
                response.append(line).append("\n");
                line = in.readLine();
            }

            return response.toString();
        }
    }

    /**
     * Get the headers in the HTTP message
     *
     * @param message
     * @return
     */
    public static String getHeaderBlock(String message) {
        Objects.requireNonNull(message);

        String[] lines = message.split("\n");
        StringBuilder headers = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                //empty line defines the end of the header section
                break;
            }
            headers.append(lines[i]).append("\n");
        }
        return headers.toString();
    }

    /**
     * Get the body of the HTTP message (if any).
     * Body block comes after the headers
     *
     * @param message
     * @return
     */
    public static String getBodyBlock(String message) {
        Objects.requireNonNull(message);

        String[] lines = message.split("\n");
        StringBuilder body = new StringBuilder();

        boolean isHeader = true;

        for (int i = 0; i < lines.length; i++) {
            if (isHeader && lines[i].isEmpty()) {
                isHeader = false;
                continue;
            }
            if (!isHeader) {
                body.append(lines[i]).append("\n");
            }
        }
        return body.toString();
    }

    /**
     * @param message
     * @param name    the name of the header, eg "Content-Type"
     * @return {@code null} if the header is not present, otherwise its value as string
     */
    public static String getHeaderValue(String message, String name) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(message);

        String[] lines = message.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String h = lines[i];
            if (h.isEmpty()) {
                break;
            }
            if (h.toLowerCase().startsWith(name.toLowerCase())) {
                int splitPoint = h.indexOf(':');
                return h.substring(splitPoint + 1, h.length()).trim();
            }
        }
        return null;
    }


    public static String getCookie(String message, String cookieName) {

        String cookies = getHeaderValue(message, "Set-Cookie");

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies.split(";"))
                .filter(s -> s.trim().startsWith(cookieName + "="))
                .findAny()
                .orElse(null);

    }

    public static String getSessionCookie(String message) {
        return getCookie(message, "JSESSIONID");
    }
}