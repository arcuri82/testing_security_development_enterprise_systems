package org.tsdes.spring.frontend.restwsamqp.zuulws

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.messaging.simp.SimpMessagingTemplate

@EnableZuulProxy
@SpringBootApplication
class GatewayApplication(
        val webSocket : SimpMessagingTemplate
) {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("number-of-rest-calls")
    }

    @Bean
    fun queue(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun binding(fanout: FanoutExchange,
                queue: Queue): Binding {
        return BindingBuilder.bind(queue).to(fanout)
    }


    @RabbitListener(queues = arrayOf("#{queue.name}"))
    fun receiveFromAMQP(msg: String) {

        webSocket.convertAndSend("/topic/restcalls", msg)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(GatewayApplication::class.java, *args)
}