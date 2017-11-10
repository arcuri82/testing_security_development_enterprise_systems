package org.tsdes.spring.security.distributedsession.zuul

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by arcuri82 on 10-Nov-17.
 */
@EnableZuulProxy
@SpringBootApplication
class GatewayApplication{
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}


fun main(args: Array<String>) {
    SpringApplication.run(GatewayApplication::class.java, *args)
}