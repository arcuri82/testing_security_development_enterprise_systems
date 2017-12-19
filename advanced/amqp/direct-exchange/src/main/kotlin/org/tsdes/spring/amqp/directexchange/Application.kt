package org.tsdes.spring.amqp.directexchange

import org.springframework.amqp.core.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    /*
        Here using a Direct exchange, but
        not the default one
     */
    @Bean
    fun direct(): DirectExchange {
        return DirectExchange("tut.direct")
    }

    /*
        Creating 2 different queues, X and Y
     */

    @Bean
    fun queueX(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun queueY(): Queue {
        return AnonymousQueue()
    }

    /*
        When a message arrive to a Direct exchange, it can be
        copied to 0, 1 or more queues.
        This depends on the "routing keys", which is usually
        the name of the queue.
        However, we can have custom keys.

        In this example, we bind X and Y to ERROR,
        whereas WARN only to Y.
        We also have a key INFO (see Sender), but no queue
        associated with it.

        So, when receiving message of key:
        INFO -> ignored
        WARN -> copied to Y
        ERROR -> copied to both X and Y
     */


    @Bean
    fun bindingX_ERROR(direct: DirectExchange,
                 queueX: Queue): Binding {
        return BindingBuilder
                .bind(queueX)
                .to(direct)
                .with("ERROR")
    }

    @Bean
    fun bindingY_ERROR(direct: DirectExchange,
                 queueY: Queue): Binding {
        return BindingBuilder
                .bind(queueY)
                .to(direct)
                .with("ERROR")
    }


    @Bean
    fun bindingY_WARN(direct: DirectExchange,
                 queueY: Queue): Binding {
        return BindingBuilder
                .bind(queueY)
                .to(direct)
                .with("WARN")
    }

}