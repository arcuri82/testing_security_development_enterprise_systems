package org.tsdes.advanced.graphql.newsgraphql

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication(scanBasePackages = ["org.tsdes.advanced"])
@EnableJpaRepositories(basePackages = ["org.tsdes.advanced"])
@EntityScan(basePackages = ["org.tsdes.advanced"])
class NewsGraphQLApplication


/*
    API accessible at
    http://localhost:8080/graphql

    UI accessible at
    http://localhost:8080/graphiql
    (note the "i" between graph and ql...)

    UI graph representation at
    http://localhost:8080/voyager
 */
fun main(args: Array<String>) {
    SpringApplication.run(NewsGraphQLApplication::class.java, *args)
}