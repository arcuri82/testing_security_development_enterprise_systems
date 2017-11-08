package org.tsdes.spring.security.database

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@SpringBootApplication
@ComponentScan
class Application


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

