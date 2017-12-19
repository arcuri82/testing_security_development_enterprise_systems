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

    /*
        In the Fanout exchange, messages are copied
        to all the bound queues.

        Note 1: on a RabbitMQ server, there can be
        many fanout exchanges.
        To distinguish them, names are used.

        Note 2: in a fanout exchange, the name of
        the queues is irrelevant, as such info is not
        used.

        Note 3: a sender just need to know of the fanout.
        A receiver needs to know about the fanout and just
        its own queue.
     */

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
    fun queueNameHolder(): QueueNameHolder {
        return QueueNameHolder()
    }

    /*
        Here I am creating beans directly with a constructor.
        But how to access other beans in such a method?
        We just autowire as input parameters... and
        Spring will take care of it.
     */

    @Bean
    fun worker0(
            @Autowired queue1: Queue,
            @Autowired queueNameHolder: QueueNameHolder
    ): WorkReceiver {

        /*
            See WorkReceiver for explanation of this.
            Note: only needed because we are running everything
            (ie 1 sender and 2 receivers) in the same Spring application.
         */
        queueNameHolder.name = queue1.name
        return WorkReceiver("a")
    }

    /*
        Note that the @Autowired annotation on the input
        beans is not strictly necessary, as Spring will automatically
        treat all input parameters as beans to inject.
     */

    @Bean
    fun worker1(queue2: Queue,
                queueNameHolder: QueueNameHolder): WorkReceiver {
        queueNameHolder.name = queue2.name
        return WorkReceiver("b")
    }


    @Bean
    fun queue1(): Queue {
        return AnonymousQueue()
    }

    @Bean
    fun queue2(): Queue {
        return AnonymousQueue()
    }

    /*
        Once we create 2 different anonymous queues,
        we need to bind them to the our fanout exchange.

        But how does Spring know that in the following 2
        beans we need to inject two different queues?

        When autowiring, the autowiring is based on class type.
        When more than one bean is of the same type, the binding
        is done by name.
        The name of the bean is given by the name of the
        @Bean method that created it.
        In the input parameters here, we need to use the same
        names.
        If you change such name (eg "foo: Queue"), then Spring
        initialization will fail.
     */

    @Bean
    fun binding1(fanout: FanoutExchange,
                 queue1: Queue): Binding {
        /*
            This will create on RabbitMQ a queue that is bound
            to the given fanout.
         */
        return BindingBuilder.bind(queue1).to(fanout)
    }

    @Bean
    fun binding2(fanout: FanoutExchange,
                 queue2: Queue): Binding {
        return BindingBuilder.bind(queue2).to(fanout)
    }


}