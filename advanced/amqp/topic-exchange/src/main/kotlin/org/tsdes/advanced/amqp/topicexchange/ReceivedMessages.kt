package org.tsdes.advanced.amqp.topicexchange

import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by arcuri82 on 09-Aug-17.
 */
@Service
class ReceivedMessages {

    private val buffer: MutableList<String> = CopyOnWriteArrayList<String>()

    val data: List<String>
        get() = buffer

    private var latch: CountDownLatch = CountDownLatch(0)

    fun addMessage(msg: String) {
        buffer.add(msg)
        latch.countDown()
    }


    fun reset(n: Int) {
        buffer.clear()
        latch = CountDownLatch(n)
    }

    fun await(seconds: Int): Boolean {
        return latch.await(seconds.toLong(), TimeUnit.SECONDS)
    }
}