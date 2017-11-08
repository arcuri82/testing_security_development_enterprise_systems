package org.tsdes.spring.security.database

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@SpringBootApplication
@ComponentScan
class Application{

    /**
     * Make sure we use BCrypt to encode/hash the password.
     * To override the default encoding, we need to provide
     * a bean with name passwordEncoder
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

