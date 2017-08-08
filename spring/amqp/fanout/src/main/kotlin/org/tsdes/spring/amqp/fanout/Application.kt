package org.tsdes.spring.amqp.fanout

import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


/**
 * Created by arcuri82 on 07-Aug-17.
 */
@SpringBootApplication
class Application {

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("tsdes.amqp.fanout")
    }

    @Bean
    fun sender(): WorkSender {
        return WorkSender()
    }

    @Bean
    fun counter(): Counter {
        return Counter()
    }

    @Bean
    fun queueNameHolder(): QueueNameHolder{
        return QueueNameHolder()
    }

    @Bean
    fun worker0(@Autowired autoDeleteQueue1: Queue,
                @Autowired queueNameHolder: QueueNameHolder): WorkReceiver {
        queueNameHolder.name = autoDeleteQueue1.name
        return WorkReceiver("a")
    }

    @Bean
    fun worker1(@Autowired autoDeleteQueue2: Queue,
                @Autowired queueNameHolder: QueueNameHolder): WorkReceiver {
        queueNameHolder.name = autoDeleteQueue2.name
        return WorkReceiver("b")
    }


    @Bean
    fun autoDeleteQueue1(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun autoDeleteQueue2(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun binding1(fanout: FanoutExchange,
                 autoDeleteQueue1: Queue): Binding {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout)
    }

    @Bean
    fun binding2(fanout: FanoutExchange,
                 autoDeleteQueue2: Queue): Binding {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout)
    }



}