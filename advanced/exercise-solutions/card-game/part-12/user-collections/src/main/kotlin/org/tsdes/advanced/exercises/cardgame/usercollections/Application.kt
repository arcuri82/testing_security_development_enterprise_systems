package org.tsdes.advanced.exercises.cardgame.usercollections

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["org.tsdes.advanced"])
class Application {
}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}