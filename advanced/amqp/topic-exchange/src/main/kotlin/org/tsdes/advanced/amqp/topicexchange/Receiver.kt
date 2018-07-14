package org.tsdes.advanced.amqp.topicexchange

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by arcuri82 on 07-Aug-17.
 */
@Service
class Receiver {

    @Autowired
    private lateinit var messages: ReceivedMessages

    @RabbitListener(queues = ["#{queueX.name}"])
    fun receiverX(msg: String) {
        doWork("X", msg)
    }

    @RabbitListener(queues = ["#{queueY.name}"])
    fun receiverY(msg: String) {
        doWork("Y", msg)
    }

    private fun doWork(receiver: String, msg: String){

        val s = "$receiver received: '$msg'"

        messages.addMessage(s)
    }
}