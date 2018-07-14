package org.tsdes.advanced.amqp.basequeue

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate

/**
 * Created by arcuri82 on 07-Aug-17.
 */
class Receiver(val host: String, val port: Int) {

    fun receive(queueName: String) : String?{

        val connectionFactory = CachingConnectionFactory()
        connectionFactory.host = host
        connectionFactory.port = port
        val admin = RabbitAdmin(connectionFactory)

        /*
            When we "declare" a queue, an actual queue
            will be created on RabbitMQ (in our case).
            It can be created either by the sender or
            the receiver, whoever is first.
            When one tries to declare a queue that
            already exists, nothing happens, unless
            the setting of the queue are different (in
            which case you would get an error).
         */

        admin.declareQueue(Queue(queueName))

        val template = RabbitTemplate(connectionFactory)

        //this is non-blocking. return "null" if there is no message
        val msg = template.receiveAndConvert(queueName) as String?

        return msg
    }
}