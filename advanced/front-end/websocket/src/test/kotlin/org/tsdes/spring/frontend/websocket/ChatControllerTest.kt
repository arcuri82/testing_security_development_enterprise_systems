package org.tsdes.spring.frontend.websocket

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

    @LocalServerPort
    private var port = 0

    /*
        Usually you will deal with WS on the browser with JavaScript.
        However, for testing, we can have a client in Java.
     */

    companion object {

        private lateinit var stompClient: WebSocketStompClient

        @BeforeClass
        @JvmStatic
        fun initializeTests() {
            val transports = listOf(WebSocketTransport(StandardWebSocketClient()))
            val sockJsClient = SockJsClient(transports)

            stompClient = WebSocketStompClient(sockJsClient)
            //specify that in the payload we use JSON
            stompClient.messageConverter = MappingJackson2MessageConverter()
        }
    }


    @Test
    fun testWebsocket() {

        val receivedBack = mutableListOf<String>()

        val latch = CountDownLatch(2)
        val lambda = { id: String, dto: MessageDto ->
            println("Handling incoming message")
            receivedBack.add("$id: '$dto'")
            latch.countDown()
         }

        val url = "ws://localhost:$port/websocket-endpoint"

        //creating two different clients
        val first = stompClient.connect(url, MyStompHandler("First", lambda), WebSocketHttpHeaders()).get()
        val second = stompClient.connect(url, MyStompHandler("Second", lambda), WebSocketHttpHeaders()).get()

        assertTrue(first.isConnected)
        assertTrue(second.isConnected)

        //send a single message to server from one client
        first.send("/ws-api/message", MessageDto("1st","hello"))

        val ok = latch.await(5000, TimeUnit.MILLISECONDS)
        assertTrue(ok)

        //both clients should get the message back from the server
        assertEquals(2, receivedBack.size)
        assertTrue(receivedBack.any { it.startsWith("First") })
        assertTrue(receivedBack.any { it.startsWith("Second") })
    }


    class MyStompHandler(
            val id: String,
            val lambda: (String, MessageDto) -> Any
    ) : StompSessionHandlerAdapter() {

        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders?) {

            /*
                Specify that, once a WS connection is established with with the server,
                we register a callback every time messages are broadcast on the
                "/topic/messages" topic.
             */

            session.subscribe("/topic/messages", object : StompFrameHandler {

                override fun getPayloadType(headers: StompHeaders): Type {
                    return MessageDto::class.java
                }

                override fun handleFrame(headers: StompHeaders, payload: Any) {
                    val dto = payload as MessageDto
                    lambda.invoke(id, dto)
                }
            })
        }

        override fun handleException(s: StompSession?, c: StompCommand?, h: StompHeaders?, p: ByteArray?, ex: Throwable?) {
            println("ERROR exception: $ex")
        }

        override fun handleTransportError(session: StompSession?, ex: Throwable?) {
            println("ERROR transport: $ex")
        }

    }
}