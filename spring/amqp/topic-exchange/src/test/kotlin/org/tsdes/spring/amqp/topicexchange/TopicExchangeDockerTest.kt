package org.tsdes.spring.amqp.topicexchange

import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TopicExchangeDockerTest {

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
    fun testTopicExchange(){

        messages.reset(5)

        val textForNewsInNorwayBySmith = "fcdnsikfbjehfbjebjg"

        sender.publish("smith","usa","sport","a")
        sender.publish("smith","norway","politics",textForNewsInNorwayBySmith)
        sender.publish("black","norway","politics","c")
        sender.publish("white","norway","science","d")
        sender.publish("white","usa","science","d")
        sender.publish("white","germany","sport","e")
        sender.publish("white","germany","politics","e")


        val completed = messages.await(2)
        Assert.assertTrue(completed)

        Assert.assertEquals(2, messages.data.filter { it.contains("X") }.count())
        Assert.assertEquals(3, messages.data.filter { it.contains("Y") }.count())
        Assert.assertEquals(5, messages.data.size)
        Assert.assertEquals(2, messages.data.filter { it.contains(textForNewsInNorwayBySmith) }.count())
    }
}