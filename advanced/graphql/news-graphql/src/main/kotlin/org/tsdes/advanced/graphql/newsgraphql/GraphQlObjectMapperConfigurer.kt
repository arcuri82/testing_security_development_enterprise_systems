package org.tsdes.advanced.graphql.newsgraphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import graphql.servlet.ObjectMapperConfigurer
import org.springframework.stereotype.Component


/**
 * GraphQL does not use Jackson configuration in Spring
 *
 * https://github.com/graphql-java/graphql-spring-boot/issues/65
 *
 * this means we need to explicitly activate the support for
 * time date serialization.
 */
@Component
class GraphQlObjectMapperConfigurer : ObjectMapperConfigurer {

    override fun configure(mapper: ObjectMapper) {
        mapper.registerModule(JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }
}