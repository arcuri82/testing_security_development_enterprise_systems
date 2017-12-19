package org.tsdes.spring.amqp.rest.receiver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@SpringBootApplication
class ReceiverApplication

fun main(args: Array<String>) {
    SpringApplication.run(ReceiverApplication::class.java, *args)
}