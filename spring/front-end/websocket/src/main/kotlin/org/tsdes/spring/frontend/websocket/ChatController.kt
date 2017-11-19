package org.tsdes.spring.frontend.websocket

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class ChatController{

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    fun handleMessage(dto: MessageDto): String {
        return "${dto.author}: ${dto.text}"
    }
}