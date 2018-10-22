package org.tsdes.advanced.security.distributedsession.scg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient


@SpringBootApplication
class GatewayApplication {
}


fun main(args: Array<String>) {
    SpringApplication.run(GatewayApplication::class.java, *args)
}