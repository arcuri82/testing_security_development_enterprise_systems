package org.tsdes.spring.amqp.distributedwork

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired


/**
 * Created by arcuri82 on 07-Aug-17.
 */
class WorkSender {

    /*
        I am autowiring an instance of RabbitTemplate,
        so that it gets automatically initialized based
        on the Spring configuration.
     */
    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var queue: Queue


    fun send(list: List<Long>) {
        list.forEach { send(it) }
    }

    private fun send(msg: Long) {
        template.convertAndSend(queue.name, msg)
    }
}