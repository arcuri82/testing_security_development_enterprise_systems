package org.tsdes.advanced.frontend.websocket.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class WebSocketBackendApplication {

}


fun main(args: Array<String>) {
    SpringApplication.run(WebSocketBackendApplication::class.java, *args)
}