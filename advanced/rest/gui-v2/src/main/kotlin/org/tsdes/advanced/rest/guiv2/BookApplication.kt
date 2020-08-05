package org.tsdes.advanced.rest.guiv2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.PathSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@SpringBootApplication
class BookApplication {

    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.OAS_30)
                .select()
                .paths(PathSelectors.any())
                .build()
    }
}


/*
    If you run this directly, you can then check the Swagger documentation at:

    http://localhost:8080/swagger-ui/index.html
 */
fun main(args: Array<String>) {
    SpringApplication.run(BookApplication::class.java, *args)
}