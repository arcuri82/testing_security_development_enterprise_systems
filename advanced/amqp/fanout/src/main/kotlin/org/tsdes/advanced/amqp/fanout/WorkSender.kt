package org.tsdes.advanced.amqp.fanout

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired


/**
 * Created by arcuri82 on 07-Aug-17.
 */
class WorkSender {

    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange


    fun send(list: List<Long>) {
        list.forEach { send(it) }
    }

    private fun send(msg: Long) {

        /*
            AMQP does not send to a Queue, but rather to an
            Exchange. Before, we did not specify an exchange,
            and so the default one was used.
            Inside an exchange, a routing key can be used to
            specify where the message should go, eg the name of
            a queue.

            In a fanout exchange, the routing key is superfluous,
            as the message is sent to all the registered queues
         */
        val exchangeName = fanout.name
        val routingKey = ""

        template.convertAndSend(exchangeName, routingKey, msg)
    }
}