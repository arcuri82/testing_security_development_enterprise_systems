package org.tsdes.spring.amqp.distributedwork

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
//import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

/**
 * Created by arcuri82 on 07-Aug-17.
 */

//FIXME: this is broken in SpringBoot 2.x, but works fine in 1.5
@Ignore
@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(initializers = [(WorkerDockerTest.Companion.Initializer::class)])
class WorkerDockerTest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule
        @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)

        /*
            We have started RabbitMQ, and we need to inform Spring of the host and port.
            This would be in properties in the "application.properties" file.
            But as these might vary (especially the port), we need to modify these
            properties on the fly, once Spring is already running and RabbitMQ has
            been started in Docker.
         */
        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                //FIXME
//                TestPropertyValues
//                        .of("spring.rabbitmq.host=" + rabbitMQ.containerIpAddress,
//                                "spring.rabbitmq.port=" + rabbitMQ.getMappedPort(5672))
//                        .applyTo(configurableApplicationContext.environment)
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

        /*
            Going to send N messages, where the processing
            of the first one will take more time than
            all the others put together
         */

        val list = listOf(5000L, 200, 200, 200, 200, 200, 200, 200, 200, 200)

        counter.reset(list.size)

        sender.send(list)

        //let's wait until everything is processed
        val completed = counter.await(10)
        assertTrue(completed)

        val data = counter.retrieveJobsDone()

        assertEquals(2, data.size)
        assertEquals(list.size, data.values.sum())
        assertTrue(data.any { it.value == 1 })
        assertTrue(data.any { it.value == 9 })

        /*
            While the first worker to pull from the
            queue will process the longest task, the other worker
            will pull and process all the other remaining tasks
         */
    }
}