package org.tsdes.advanced.amqp.directexchange


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest
@ContextConfiguration(initializers = [(DirectExchangeDockerTest.Companion.Initializer::class)])
class DirectExchangeDockerTest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @Container
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

    @Autowired
    private lateinit var sender: Sender

    @Autowired
    private lateinit var messages: ReceivedMessages


    @Test
    fun testDirectExchange() {

        messages.reset(3)

        sender.info("a")
        sender.warn("b")
        sender.error("c")

        val completed = messages.await(2)
        assertTrue(completed)

        assertEquals(0, messages.data.filter { it.contains("INFO") }.count())
        assertEquals(1, messages.data.filter { it.contains("WARN") }.count())
        assertEquals(2, messages.data.filter { it.contains("ERROR") }.count())
    }
}