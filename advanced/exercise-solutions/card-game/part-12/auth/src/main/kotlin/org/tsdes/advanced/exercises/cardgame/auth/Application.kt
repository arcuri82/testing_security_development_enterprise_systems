package org.tsdes.advanced.exercises.cardgame.auth

import org.springframework.amqp.core.FanoutExchange
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by arcuri82 on 10-Nov-17.
 */
@SpringBootApplication
class Application{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun fanout(): FanoutExchange {
        return FanoutExchange("user-creation")
    }
}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}