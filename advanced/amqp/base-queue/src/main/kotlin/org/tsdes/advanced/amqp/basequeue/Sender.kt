package org.tsdes.advanced.amqp.basequeue

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate


/**
 * Created by arcuri82 on 07-Aug-17.
 */
class Sender(val host: String, val port: Int) {

    fun send(queueName: String, msg: String) {

        /*
            Note: no Spring application here.
            Using Rabbit library to directly communicate
            with the RabbitMQ server.
         */

        val connectionFactory = CachingConnectionFactory()
        connectionFactory.host = host
        connectionFactory.port = port
        val admin = RabbitAdmin(connectionFactory)

        /*
            AMQP server (eg RabbitMQ) will have "exchanges",
            which are entry points.
            An "exchange" can have 1 or more queues.
            When we create a queue, if we do not specify the
            exchange, it will bind to the default one.

            The default exchange is of type "direct".
         */

        admin.declareQueue(Queue(queueName))

        val template = RabbitTemplate(connectionFactory)
        template.convertAndSend(queueName, msg)
    }
}