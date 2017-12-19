package org.tsdes.spring.microservice.discovery.producer

import org.springframework.beans.factory.annotation.Value
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
@EnableEurekaClient  //tell Eureka to register this service
@RestController
class ProducerApplication {


    @GetMapping(path = arrayOf("producerData"),
            produces = arrayOf(MediaType.TEXT_PLAIN_VALUE))
    fun get() : ResponseEntity<String>{

        /*
            Here, I am going to give a different response based on
            an environment variable, which I do set in the docker-compose.yml
            file.
         */

        val id = (System.getenv("PRODUCER_ID") ?: "Undefined").trim()

        return ResponseEntity.ok(id)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ProducerApplication::class.java, *args)
}
