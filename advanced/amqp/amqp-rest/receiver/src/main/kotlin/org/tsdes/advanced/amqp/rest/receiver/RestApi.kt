package org.tsdes.advanced.amqp.rest.receiver

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 11-Aug-17.
 */
@RestController
class RestApi {

    /*
        Note: this is just an example... as you should
        not have internal state in a REST API...
     */
    private var counter = 0

    /*
        Increase a counter every time we receive a broadcast
        message from RabbitMQ
     */

    @RabbitListener(queues = ["#{queue.name}"])
    fun receiveFromAMQP(msg: String) {

        print(msg)

        counter++
    }

    /*
        REST endpoint to read the counter of RabbitMQ messages
        that have been received so far.
     */

    @GetMapping(path = ["/counter"])
    fun get(): ResponseEntity<Int> =  ResponseEntity.ok(counter)

}