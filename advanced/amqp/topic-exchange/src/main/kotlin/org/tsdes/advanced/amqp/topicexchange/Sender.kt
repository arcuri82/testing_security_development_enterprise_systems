package org.tsdes.advanced.amqp.topicexchange

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@Service
class Sender {

    @Autowired
    private lateinit var  template: RabbitTemplate

    @Autowired
    private lateinit var topic: TopicExchange

    private fun send(msg: String, key: String){
        template.convertAndSend(topic.name, key, msg);
    }

    fun publish(author: String, country: String, kind: String, text: String) {
        send(text, "$author.$country.$kind")
    }

}