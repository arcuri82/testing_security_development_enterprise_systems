package org.tsdes.advanced.microservice.gateway.scg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient


@EnableDiscoveryClient
@SpringBootApplication
class GatewayApplication {
}


fun main(args: Array<String>) {
    SpringApplication.run(GatewayApplication::class.java, *args)
}