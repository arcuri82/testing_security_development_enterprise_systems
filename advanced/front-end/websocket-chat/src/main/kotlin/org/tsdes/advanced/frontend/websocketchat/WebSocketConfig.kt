package org.tsdes.advanced.frontend.websocketchat

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

/*
    This creates an embedded broker for the WS messages
 */
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        /*
            specify the prefix for the type of topics that can be read in this WS.
            this works like a broadcast, similarly to topics in AMQP
         */
        config.enableSimpleBroker("/topic")
        /*
            specify the prefix of where to send direct, not-broadcast messages,
            usually from client (eg browser) to server (ie, this broker)
         */
        config.setApplicationDestinationPrefixes("/ws-api")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        /*
            Specify the endpoint on this server for where the client needs
            to make a HTTP connection for sending/receiving WS messages.
            Note: the first time a connection is established is in HTTP, but
            then the protocol does switch to WS
         */
        registry.addEndpoint("/websocket-endpoint").withSockJS()
    }
}