package org.tsdes.spring.amqp.topicexchange

import org.springframework.amqp.core.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    @Bean
    fun topic(): TopicExchange {
        return TopicExchange("tut.topic")
    }

    @Bean
    fun autoDeleteQueueX(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun autoDeleteQueueY(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun bindingX(topic: TopicExchange,
                 autoDeleteQueueX: Queue): Binding {
        return BindingBuilder
                .bind(autoDeleteQueueX)
                .to(topic)
                .with("smith.#")
    }

    @Bean
    fun bindingY(topic: TopicExchange,
                 autoDeleteQueueY: Queue): Binding {
        return BindingBuilder
                .bind(autoDeleteQueueY)
                .to(topic)
                .with("*.norway.*")
    }


}