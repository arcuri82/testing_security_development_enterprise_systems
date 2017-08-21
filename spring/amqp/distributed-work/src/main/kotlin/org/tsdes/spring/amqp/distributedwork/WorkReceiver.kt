package org.tsdes.spring.amqp.distributedwork

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by arcuri82 on 07-Aug-17.
 */
@RabbitListener(queues = arrayOf(QUEUE_NAME))
class WorkReceiver(val id: String) {

    @Autowired
    private lateinit var counter: Counter

    @RabbitHandler
    fun doWork(x: java.lang.Long){

        println("Worker '$id' going to do work with value: $x")

        Thread.sleep(x.toLong())

        counter.doneJob(id)
    }
}