package org.tsdes.spring.amqp.distributedwork

import org.springframework.amqp.core.Queue
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


const val QUEUE_NAME = "queue:distributed-work"

/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    /*
        By using @Bean annotated method here, I
        create beans that are going to be proxied
        by Spring.
        The point here is that I can use constructor
        to create my instances with my chosen input
        values, and executing any necessary initializing
        code.

        These beans can be autowired in other beans.
     */

    @Bean
    fun queue(): Queue {
        return Queue(QUEUE_NAME)
    }

    /*
        Here I am creating more than 1 instance of WorkReceiver
        for beans.
        I need it, because I want each instance having a
        different id, which I pass in the constructor.
        This is fine, as these beans will be treated by Spring as
        separated singletons.
        If I need to autowire them in other beans, I would
        get an error, as which one of the 2 instances to use?
        This can be done by using "names" when autowiring (we
        ll see examples in later modules).
     */

    @Bean
    fun worker0(): WorkReceiver {
        return WorkReceiver("a")
    }

    @Bean
    fun worker1(): WorkReceiver {
        return WorkReceiver("b")
    }

    @Bean
    fun sender(): WorkSender {
        return WorkSender()
    }

    @Bean
    fun counter(): Counter {
        return Counter()
    }
}