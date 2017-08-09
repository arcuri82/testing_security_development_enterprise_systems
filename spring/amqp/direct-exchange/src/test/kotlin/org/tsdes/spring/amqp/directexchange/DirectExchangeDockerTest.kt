package org.tsdes.spring.amqp.directexchange

import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer
import org.junit.Assert.*

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class DirectExchangeDockerTest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)
    }

    @Autowired
    private lateinit var sender: Sender

    @Autowired
    private lateinit var messages: ReceivedMessages


    @Test
    fun testDirectExchange(){

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