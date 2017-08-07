package org.tsdes.spring.amqp.distributedwork

import org.springframework.amqp.core.Queue
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.tsdes.spring.amqp.basequeue.WorkReceiver
import org.tsdes.spring.amqp.basequeue.WorkSender


const val QUEUE_NAME = "queue:distributed-work"

/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    @Bean
    fun queue(): Queue {
        return Queue(QUEUE_NAME)
    }

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
    fun counter(): Counter{
        return Counter()
    }
}