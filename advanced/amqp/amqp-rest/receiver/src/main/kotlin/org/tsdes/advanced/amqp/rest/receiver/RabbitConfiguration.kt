package org.tsdes.advanced.amqp.rest.receiver

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by arcuri82 on 11-Aug-17.
 */
@Configuration
class RabbitConfiguration {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("tsdes.amqp.rest")
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

}