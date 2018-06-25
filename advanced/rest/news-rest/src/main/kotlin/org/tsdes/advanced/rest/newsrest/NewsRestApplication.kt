package org.tsdes.advanced.rest.newsrest

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
 * You need to mark a class with @SpringBootApplication.
 * This will be the entry point for the Spring Boot application.
 * When started it will recursively scan the classpath of current
 * package and subpackages for beans to instantiate.
 *
 * One problem here is that we are in "org.tsdes.spring.rest.newsrest",
 * which is not an ancestor of the "org.tsdes.spring.examples.news"
 * library module we want to use.
 * Therefore, we need to explicitly tell Spring which packages to scan.
 * In our case, their common ancestor is "org.tsdes.spring".
 * However, in that case, we also have to do the same for
 * @EnableJpaRepositories and @EntityScan.
 * These latter two annotations would not had been needed if
 * @SpringBootApplication was in an ancestor package
 *
 * Created by arcuri82 on 06-Jul-17.
 */
@SpringBootApplication(scanBasePackages = ["org.tsdes"])
@EnableJpaRepositories(basePackages = ["org.tsdes"])
@EntityScan(basePackages = ["org.tsdes"])
@EnableSwagger2 //needed to enable Swagger
class NewsRestApplication {


    /*
        Bean used to configure Swagger documentation
     */
    @Bean
    fun swaggerApi(): Docket {
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


    /*
        FIXME this might no longer be necessary in Spring Boot 2
     */

    /*
        Bean used to configure how JSON un/marshalling is done.
     */
    @Bean(name = ["OBJECT_MAPPER_BEAN"])
    fun jsonObjectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
                /*
                    JSON does not specify how dates should be represented, whereas JavaScript does.
                    And in JavaScript it is ISO 8601.
                    So, to represent dates to send over a network consumed by different clients,
                    it is reasonable to send them in ISO 8601 instead of a numeric timestamp.
                    Here we make sure timestamps are not used in marshalling of JSON data.

                    Example:
                    2001-01-05T13:15:30Z
                 */
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
                //make sure we can use Java 8 dates
                .modules(JavaTimeModule())
                .build()
    }
}

/*
    If you run this directly, you can then check the Swagger documentation at:

    http://localhost:8080/newsrest/api/swagger-ui.html

 */
fun main(args: Array<String>) {
    SpringApplication.run(NewsRestApplication::class.java, *args)
}