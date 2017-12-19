package org.tsdes.spring.frontend.websocket

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by arcuri82 on 16-Nov-17.
 */
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
