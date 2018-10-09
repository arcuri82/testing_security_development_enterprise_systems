package org.tsdes.advanced.frontend.websocket

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class ChatController{

    /*
        Note: this is slightly confusing.
        When we refer to direct messages, the prefix is not specified.
        In other words, here we write "/message" instead of "/ws-api/message".
        However, for topics (ie broadcast), we need the prefix, ie "/topic/messages".
        The prefix are defined in the WebSocketConfig class.
     */

    @MessageMapping("/message") // register handler for incoming direct messages on "/ws-api/message"
    @SendTo("/topic/messages") // once this method is completed, the output is going to be broadcast
    fun handleMessage(dto: MessageDto): MessageDto {

        /*
            Every time we receive a message from one client on "/ws-api/message",
            we do broadcast it to all listeners for the "/topic/messages" topic.
         */

        return dto
    }
}