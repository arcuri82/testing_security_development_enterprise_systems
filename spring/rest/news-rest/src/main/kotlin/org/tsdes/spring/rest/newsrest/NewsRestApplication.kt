package org.tsdes.spring.rest.newsrest

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


/**
 * TODO explain reason for using basePackages
 *
 * Created by arcuri82 on 06-Jul-17.
 */
@SpringBootApplication(scanBasePackages = arrayOf("org.tsdes.spring"))
@EnableJpaRepositories(basePackages = arrayOf("org.tsdes.spring"))
@EntityScan(basePackages = arrayOf("org.tsdes.spring"))
@EnableSwagger2
class NewsRestApplication {

    @Bean
    fun piApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("API for REST News")
                .description("Some description")
                .version("1.0")
                .build()
    }

    @Bean(name = arrayOf("OBJECT_MAPPER_BEAN"))
    fun jsonObjectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
                .modules(JavaTimeModule())
                .build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(NewsRestApplication::class.java, *args)
}