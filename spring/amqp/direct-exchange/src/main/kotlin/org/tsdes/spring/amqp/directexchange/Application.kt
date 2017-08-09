package org.tsdes.spring.amqp.directexchange

import org.springframework.amqp.core.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    @Bean
    fun direct(): DirectExchange {
        return DirectExchange("tut.direct")
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
    fun bindingX_ERROR(direct: DirectExchange,
                 autoDeleteQueueX: Queue): Binding {
        return BindingBuilder
                .bind(autoDeleteQueueX)
                .to(direct)
                .with("ERROR")
    }

    @Bean
    fun bindingY_ERROR(direct: DirectExchange,
                 autoDeleteQueueY: Queue): Binding {
        return BindingBuilder
                .bind(autoDeleteQueueY)
                .to(direct)
                .with("ERROR")
    }


    @Bean
    fun bindingY_WARN(direct: DirectExchange,
                 autoDeleteQueueY: Queue): Binding {
        return BindingBuilder
                .bind(autoDeleteQueueY)
                .to(direct)
                .with("WARN")
    }

}