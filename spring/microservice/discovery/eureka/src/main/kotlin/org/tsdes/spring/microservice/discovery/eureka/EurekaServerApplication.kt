package org.tsdes.spring.microservice.discovery.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

/**
 * Created by arcuri82 on 28-Sep-17.
 */
@SpringBootApplication
@EnableEurekaServer
class EurekaServerApplication

/*
    Once this process is started, it will provide a Web Page at
    http://localhost:8761/
    in which you can monitor the current state of the Eureka Server
 */
fun main(args: Array<String>) {
    SpringApplication.run(EurekaServerApplication::class.java, *args)
}

