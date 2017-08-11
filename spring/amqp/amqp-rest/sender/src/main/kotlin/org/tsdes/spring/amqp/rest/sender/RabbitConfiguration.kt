package org.tsdes.spring.amqp.rest.sender

import org.springframework.amqp.core.FanoutExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@Configuration
class RabbitConfiguration {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("tsdes.amqp.rest")
    }
}