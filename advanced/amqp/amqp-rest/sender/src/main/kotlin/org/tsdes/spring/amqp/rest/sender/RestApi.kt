package org.tsdes.spring.amqp.rest.sender

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@RestController
class RestApi {

    @Autowired
    private lateinit var  template: RabbitTemplate

    @Autowired
    private lateinit var fanout: FanoutExchange

    @PostMapping(path = arrayOf("/sender"))
    fun send(@RequestBody msg: String) : ResponseEntity<Void>{

        /*
            Every time we receive a POST on this endpoint,
            we broadcast the sent message to all the listeners
            for this fanout exchange
         */

        template.convertAndSend(fanout.name, "", msg)

        return ResponseEntity.status(204).build()
    }
}