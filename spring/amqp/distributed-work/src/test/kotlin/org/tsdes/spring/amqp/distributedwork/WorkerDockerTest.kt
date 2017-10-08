package org.tsdes.spring.amqp.distributedwork

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

/**
 * Created by arcuri82 on 07-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(initializers = arrayOf(WorkerDockerTest.Companion.Initializer::class))
class WorkerDockerTest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                EnvironmentTestUtils.addEnvironment(
                        "testcontainers",
                        configurableApplicationContext.environment,
                        "spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
                        "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672)
                )
            }
        }
    }

    @Autowired
    private lateinit var counter: Counter

    @Autowired
    private lateinit var sender: WorkSender


    @Test
    fun testLoadBalanced() {

        val list = listOf(2000L, 300, 1000, 300)

        counter.reset(list.size)

        sender.send(list)

        val completed = counter.await(4)
        assertTrue(completed)

        val data = counter.retrieveJobsDone()

        assertEquals(2, data.size)
        assertEquals(list.size, data.values.sum())
        assertTrue(data.any { it.value == 1 })
        assertTrue(data.any { it.value == 3 })
    }
}