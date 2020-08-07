package org.tsdes.advanced.rest.circuitbreaker

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.PathSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.time.Duration


/**
 * Created by arcuri82 on 03-Aug-17.
 */
@SpringBootApplication
class CircuitBreakerApplication {

    @Bean
    fun globalCustomConfiguration(): Customizer<Resilience4JCircuitBreakerFactory> {

        val circuitBreakerConfig = CircuitBreakerConfig.custom()
                // how many failures (in %) before activating the CB?
                .failureRateThreshold(51f)
                // the number of most recent calls on which failure rate is calculated
                .slidingWindowSize(2)
                //for how long should the CB stop requests once on?
                .waitDurationInOpenState(Duration.ofMillis(5000))
                .build()
        val timeLimiterConfig = TimeLimiterConfig.custom()
                // how long to wait before giving up a request?
                .timeoutDuration(Duration.ofMillis(500))
                .build()

        return Customizer<Resilience4JCircuitBreakerFactory> { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(timeLimiterConfig)
                        .circuitBreakerConfig(circuitBreakerConfig)
                        .build()
            }
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