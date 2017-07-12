package org.tsdes.spring.rest.newsrest

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.builders.PathSelectors.regex

/**
 * Created by arcuri82 on 06-Jul-17.
 */
@SpringBootApplication
@EnableSwagger2
class NewsRestApplication {

    @Bean
    fun piApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/.*"))
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("API for REST News")
                .description("Some description")
                .version("1.0")
                .build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(NewsRestApplication::class.java, *args)
}