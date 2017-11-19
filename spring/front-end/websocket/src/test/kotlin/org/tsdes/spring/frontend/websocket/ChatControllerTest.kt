package org.tsdes.spring.frontend.websocket

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
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


    companion object {

        private lateinit var stompClient: WebSocketStompClient

        @BeforeClass
        @JvmStatic
        fun initializeTests() {
            val transports = listOf(WebSocketTransport(StandardWebSocketClient()))
            val sockJsClient = SockJsClient(transports)

            stompClient = WebSocketStompClient(sockJsClient)
            stompClient.messageConverter = MappingJackson2MessageConverter()
        }
    }

    //FIXME
    @Test
    fun testWebsocket() {

        val receivedBack = mutableListOf<String>()

        val latch = CountDownLatch(2)
        val lambda = { id: String, msg: String -> {
            receivedBack.add("$id:$msg")
            latch.countDown()
        } }

        val url = "ws://localhost:$port/websocket-endpoint"

        val first = stompClient.connect(url, MyStompHandler("First", lambda), WebSocketHttpHeaders()).get()
        val second = stompClient.connect(url, MyStompHandler("Second", lambda), WebSocketHttpHeaders()).get()

        assertTrue(first.isConnected)
        assertTrue(second.isConnected)

        first.send("/ws-api/message", MessageDto("1st","hello"))

        val ok = latch.await(5000, TimeUnit.MILLISECONDS)
        assertTrue(ok)

        assertEquals(2, receivedBack.size)
        assertTrue(receivedBack.any { it.startsWith("First") })
        assertTrue(receivedBack.any { it.startsWith("Second") })
    }


    class MyStompHandler(
            val id: String,
            val lambda: (String, String) -> Any
    ) : StompSessionHandlerAdapter() {

        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders?) {
            val subscription = session.subscribe("/topic/messages", object : StompFrameHandler {

                override fun getPayloadType(headers: StompHeaders): Type {
                    return String::class.java
                }

                override fun handleFrame(headers: StompHeaders, payload: Any) {
                    val msg = payload as String
                    lambda.invoke(id, msg)
                }
            })

            subscription.addReceiptTask{println("Receipt received")}
            subscription.addReceiptLostTask { println("Receipt lost") }
        }

        override fun handleException(s: StompSession?, c: StompCommand?, h: StompHeaders?, p: ByteArray?, ex: Throwable?) {
            println("ERROR: $ex")
        }

        override fun handleTransportError(session: StompSession?, ex: Throwable?) {
            println("ERROR: $ex")
        }

    }
}