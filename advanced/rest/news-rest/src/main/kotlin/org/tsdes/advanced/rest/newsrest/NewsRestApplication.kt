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
 * One problem here is that we are in "org.tsdes.advanced.rest.newsrest",
 * which is not an ancestor of the "org.tsdes.advanced.examplenews"
 * library module we want to use.
 * Therefore, we need to explicitly tell Spring which packages to scan.
 * In our case, their common ancestor is "org.tsdes.advanced".
 * However, in that case, we also have to do the same for
 * @EnableJpaRepositories and @EntityScan.
 * These latter two annotations would not had been needed if
 * @SpringBootApplication was in an ancestor package
 *
 * Created by arcuri82 on 06-Jul-17.
 */
@SpringBootApplication(scanBasePackages = ["org.tsdes.advanced"])
@EnableJpaRepositories(basePackages = ["org.tsdes.advanced"])
@EntityScan(basePackages = ["org.tsdes.advanced"])
class NewsRestApplication {


    /*
        Bean used to configure Swagger documentation
     */
    @Bean
    fun swaggerApi(): Docket {
        return Docket(DocumentationType.OAS_30)
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
}

/*
    If you run this directly, you can then check the Swagger documentation at:

    http://localhost:8080/newsrest/api/swagger-ui/index.html

 */
fun main(args: Array<String>) {
    SpringApplication.run(NewsRestApplication::class.java, *args)
}