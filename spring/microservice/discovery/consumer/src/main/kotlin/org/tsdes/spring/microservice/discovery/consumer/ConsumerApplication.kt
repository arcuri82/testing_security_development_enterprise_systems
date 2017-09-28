package org.tsdes.spring.microservice.discovery.consumer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 28-Sep-17.
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
class ConsumerApplication {

    @GetMapping(path = arrayOf("consumerData"),
            produces = arrayOf(MediaType.TEXT_PLAIN_VALUE))
    fun get() : ResponseEntity<String> {

        //TODO call to Producer via Hystrix/Ribbon

        return ResponseEntity.ok("TODO")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ConsumerApplication::class.java, *args)
}
