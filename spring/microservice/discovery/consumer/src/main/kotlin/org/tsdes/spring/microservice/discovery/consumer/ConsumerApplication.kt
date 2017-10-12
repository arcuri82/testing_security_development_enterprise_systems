package org.tsdes.spring.microservice.discovery.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.ribbon.RibbonClient
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate


/**
 * Created by arcuri82 on 28-Sep-17.
 */
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "producer-server")
@RestController
class ConsumerApplication {

    @LoadBalanced
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Autowired
    private lateinit var restTemplate: RestTemplate



    @GetMapping(path = arrayOf("consumerData"),
            produces = arrayOf(MediaType.TEXT_PLAIN_VALUE))
    fun get(): ResponseEntity<String> {

        val msg = try {
            restTemplate.getForObject(
                    "http://producer-server/producerData",
                    String::class.java)
        }catch (e: Exception){
            "ERROR: $e"
        }

        return ResponseEntity.ok("Received: $msg")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ConsumerApplication::class.java, *args)
}
