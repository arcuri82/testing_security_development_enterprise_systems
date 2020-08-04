package org.tsdes.advanced.security.rememberme

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootApplication
class Application{

}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

