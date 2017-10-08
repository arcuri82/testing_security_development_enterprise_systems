package org.tsdes.spring.amqp.basequeue

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

        admin.declareQueue(Queue(queueName))

        val template = RabbitTemplate(connectionFactory)

        //this is non-blocking. return "null" if there is no message
        val msg = template.receiveAndConvert(queueName) as String?

        return msg
    }
}