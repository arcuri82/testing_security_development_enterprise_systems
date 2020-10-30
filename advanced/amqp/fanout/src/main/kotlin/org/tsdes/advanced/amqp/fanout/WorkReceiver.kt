package org.tsdes.advanced.amqp.fanout

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by arcuri82 on 07-Aug-17.
 */
class WorkReceiver(val id: String) {

    @Autowired
    private lateinit var counter: Counter

    /*
        Quite tricky workaround.
        In an annotation, because it is resolved at compilation
        time, the data has to be a constant.
        But here we need a variable, which we cannot have.
        So, we use an expression that is going to be resolved
        at runtime by Spring, which will look at a bean named
        "queueNameHolder", and read its "name" property, which
        is initialized (and overwritten several times)
        when the beans are created.
     */

    @RabbitListener(queues = ["#{queueNameHolder.name}"])
    fun receive(x: Long) {
        doWork(x)
    }

    private fun doWork(x: Long){

        println("Worker '$id' going to do work with value: $x")

        Thread.sleep(x.toLong())

        counter.doneJob(id, x.toInt())
    }
}