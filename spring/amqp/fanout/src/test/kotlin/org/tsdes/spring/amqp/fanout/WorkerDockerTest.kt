package org.tsdes.spring.amqp.fanout

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

/**
 * Created by arcuri82 on 07-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class WorkerDockerTest {

    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        @ClassRule @JvmField
        val rabbitMQ = KGenericContainer("rabbitmq:3").withExposedPorts(5672)
    }

    @Autowired
    private lateinit var counter: Counter

    @Autowired
    private lateinit var sender: WorkSender


    @Test
    fun testFanout() {

        val list = listOf(100L, 200, 400)
        val sum = list.sum().toInt()

        counter.reset(2 * list.size)

        sender.send(list)

        val completed = counter.await(2)
        assertTrue(completed)

        val data = counter.retrieveJobsDone()

        assertEquals(2, data.size)
        assertEquals(2 * sum,  data.values.sum())
        assertTrue(data.all { it.value == sum })
    }
}