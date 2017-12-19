package org.tsdes.spring.amqp.distributedwork

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired

/**
 * When we create a @Bean out of this class,
 * Spring will use this annotation to automatically
 * connect to RabbitMQ and listen to messages on the
 * given named queue.
 *
 * Note: in Application we create more than 1 bean
 * for this class. All these singletons will use
 * the same named queue.
 */
@RabbitListener(queues = arrayOf(QUEUE_NAME))
class WorkReceiver(
        private val id: String
) {

    @Autowired
    private lateinit var counter: Counter

    /*
        Method that is executed when a message is
        read from the queue.
        We do not need to do an explicit pull on the
        queue, as it will be automatically done by Spring.
     */
    @RabbitHandler
    fun doWork(x: java.lang.Long){

        println("Worker '$id' going to do work with value: $x")

        //simulate the job execution taking some time
        Thread.sleep(x.toLong())

        /*
            Inform the counter singleton that a new job was completed
            for the worker with the given "id"
         */
        counter.doneJob(id)
    }
}