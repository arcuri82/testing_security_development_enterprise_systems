package org.tsdes.advanced.amqp.rest.sender

import org.junit.Assert.*
import org.junit.ClassRule
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer


@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(initializers = [(RestApiTest.Companion.Initializer::class)])
class RestApiTest{


    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                TestPropertyValues
                        .of("spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                                "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672))
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }
}