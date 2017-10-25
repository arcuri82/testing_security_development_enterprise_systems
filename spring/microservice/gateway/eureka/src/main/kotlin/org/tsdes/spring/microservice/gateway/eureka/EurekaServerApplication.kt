package org.tsdes.spring.microservice.gateway.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer


@SpringBootApplication
@EnableEurekaServer
class EurekaServerApplication


fun main(args: Array<String>) {
    SpringApplication.run(EurekaServerApplication::class.java, *args)
}