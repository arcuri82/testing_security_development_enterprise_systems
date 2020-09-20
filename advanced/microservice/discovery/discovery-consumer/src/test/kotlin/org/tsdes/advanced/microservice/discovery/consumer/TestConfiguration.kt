package org.tsdes.advanced.microservice.discovery.consumer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestTemplate


@Configuration
class TestConfiguration {

    @Primary
    @Bean
    fun nonLoadBalancedRestTemplate(): RestTemplate {
        return RestTemplate()
    }
}