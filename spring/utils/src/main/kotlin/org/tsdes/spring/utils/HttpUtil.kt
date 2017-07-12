package org.tsdes.spring.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

/**
 * Class with methods to do low-level HTTP communications using raw TCP sockets.
 * Not something you should use, but just to understand what HTTP communications
 * actually are.
 */
class HttpUtil {

    companion object {

        /**
         * Connect to given host:port via direct TCP socket, send the input string, and
         * return the result (if any) as a string
         */
        fun executeHttpCommand(host: String, port: Int, request: String, charset: String = "UTF-8"): String {

            Socket(host, port).use { socket ->
                socket.getOutputStream().write(request.toByteArray(charset(charset)))
                socket.shutdownOutput()

                val reader = BufferedReader(InputStreamReader(socket.getInputStream(), charset))

                var response = ""
                var line: String? = reader.readLine()

                while (line != null) {
                    response += line + "\n"
                    line = reader.readLine()
                }
                return response
            }
        }

        /**
         * Get the headers in the HTTP message
         */
        fun getHeaderBlock(message: String): String {
            return message.split("\n")
                    .takeWhile { !it.isBlank() }
                    .joinToString("\n")
        }

        /**
         * Get the body of the HTTP message (if any).
         * Body block comes after the headers
         */
        fun getBodyBlock(message: String): String? {

            return message.split("\n")
                    .run {
                        indexOfFirst { it.isBlank() }
                                .let {
                                    if (it < 0 || it == lastIndex) return null
                                    else return subList(it + 1, size).joinToString("\n")
                                }
                    }
        }

        /**
         * @param name the name of the header, eg "Content-Type"
         *
         * @param message
         *
         * @return *null* if the header is not present, otherwise its value as string
         */
        fun getHeaderValue(name: String, message: String): String? {

            return message.split("\n")
                    .takeWhile { !it.isBlank() }
                    .find { it.toLowerCase().startsWith(name.toLowerCase()) }
                    ?.run { substring(indexOf(':') + 1, length).trim() }
        }
    }
}

