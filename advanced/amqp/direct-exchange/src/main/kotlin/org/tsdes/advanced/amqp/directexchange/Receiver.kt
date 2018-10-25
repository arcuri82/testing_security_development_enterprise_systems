package org.tsdes.advanced.amqp.directexchange

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

    /*
        Here I am defining two different handlers, listening
        for messages from 2 different queues.
     */


    //as anonymous queues have random names, we need to resolve them at runtime
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