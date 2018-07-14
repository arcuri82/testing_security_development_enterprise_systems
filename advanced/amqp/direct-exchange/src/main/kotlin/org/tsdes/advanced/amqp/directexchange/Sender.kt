package org.tsdes.advanced.amqp.directexchange

import org.springframework.amqp.core.DirectExchange
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
    private lateinit var direct: DirectExchange

    private fun send(msg: String, key: String){
        template.convertAndSend(direct.name, key, msg);
    }

    /*
        Here I am giving 3 different types of logging messages,
        which will be routed to the same direct exchange,
        but with different routing keys
     */

    fun info(msg: String) = send("INFO: $msg", "INFO")

    fun warn(msg: String) = send("WARN: $msg", "WARN")

    fun error(msg: String) = send("ERROR: $msg", "ERROR")
}