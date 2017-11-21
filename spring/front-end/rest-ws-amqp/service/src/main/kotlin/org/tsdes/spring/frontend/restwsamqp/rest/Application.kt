package org.tsdes.spring.frontend.restwsamqp.rest

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 20-Nov-17.
 */
@SpringBootApplication
@RestController
class Application(
        var  template: RabbitTemplate,
        var fanout: FanoutExchange
) {


    @PostMapping(path = arrayOf("/api/foo"))
    fun postOnFoo(){

        template.convertAndSend(fanout.name, "", "POST on '/api/foo'")
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}