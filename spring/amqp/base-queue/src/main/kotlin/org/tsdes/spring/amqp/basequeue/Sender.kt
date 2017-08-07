package org.tsdes.spring.amqp.basequeue

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate


/**
 * Created by arcuri82 on 07-Aug-17.
 */
class Sender {

    fun send(queueName: String, msg: String){

        val connectionFactory = CachingConnectionFactory()
        val admin = RabbitAdmin(connectionFactory)
        admin.declareQueue(Queue(queueName))

        val template = RabbitTemplate(connectionFactory)
        template.convertAndSend(queueName, msg)
    }
}