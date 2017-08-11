package org.tsdes.spring.amqp.rest.receiver

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 11-Aug-17.
 */
@RestController
class RestApi {

    private var counter = 0

    @RabbitListener(queues = arrayOf("#{autoDeleteQueue.name}"))
    fun receiveFromAMQP(msg: String) {

        print(msg)

        counter++
    }

    @GetMapping(path = arrayOf("/counter"))
    fun get(): ResponseEntity<Int> =  ResponseEntity.ok(counter)

}