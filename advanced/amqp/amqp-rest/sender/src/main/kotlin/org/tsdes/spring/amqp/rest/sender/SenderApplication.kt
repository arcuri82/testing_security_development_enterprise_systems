package org.tsdes.spring.amqp.rest.sender

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@SpringBootApplication
class SenderApplication


fun main(args: Array<String>) {
    SpringApplication.run(SenderApplication::class.java, *args)
}