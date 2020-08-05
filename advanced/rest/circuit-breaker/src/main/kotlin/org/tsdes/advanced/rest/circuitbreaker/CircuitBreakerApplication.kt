package org.tsdes.advanced.rest.circuitbreaker

import com.netflix.config.ConfigurationManager
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.PathSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Created by arcuri82 on 03-Aug-17.
 */
@SpringBootApplication
class CircuitBreakerApplication {

    init {
        //Hystrix configuration
        ConfigurationManager.getConfigInstance().apply {
            // how long to wait before giving up a request?
            setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 500) //default is 1000
            // how many failures before activating the CB?
            setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 2) //default 20
            setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage", 50)
            //for how long should the CB stop requests? after this, 1 single request will try to check if remote server is ok
            setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 5000)
        }
    }

    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.OAS_30)
                .select()
                .paths(PathSelectors.any())
                .build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CircuitBreakerApplication::class.java, *args)
}