package org.tsdes.advanced.microservice.discovery.consumer

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
@EnableEurekaClient  // Register to Eureka
@RibbonClient(name = "producer-server") // Using Ribbon for the given service on Eureka
@RestController
class ConsumerApplication {

    /*
        Ribbon is load balancing on client side.
        So, here, we register a REST template that does
        this load balancing each time it is called.

        Note: to do that, under the hoods this process will
        ask Eureka for a list of IP addresses for a given
        service (which can be replicated in different instances)
     */
    @LoadBalanced
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Autowired
    private lateinit var restTemplate: RestTemplate



    @GetMapping(path = ["consumerData"],
            produces = [(MediaType.TEXT_PLAIN_VALUE)])
    fun get(): ResponseEntity<String> {

        val msg = try {
            restTemplate.getForObject(
                    /*
                        Note here that the "host" name is not a valid one.
                        It is actually the name of a service registered on Eureka.
                        In this case, the same name used in
                        "spring.application.name" property in the
                        "producer" module
                     */
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
