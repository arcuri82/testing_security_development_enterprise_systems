package org.tsdes.spring.amqp.fanout

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Same kind of counter class as before
 *
 * Created by arcuri82 on 07-Aug-17.
 */
class Counter{

    /**
        key -> worker id
        value -> jobs done by the given worker
     */
    private val jobsDone: MutableMap<String, Int> = ConcurrentHashMap()

    private var latch : CountDownLatch = CountDownLatch(0)

    fun reset(n: Int){
        jobsDone.clear()
        latch = CountDownLatch(n)
    }

    fun doneJob(id: String, x: Int){
        jobsDone.merge(id, x, { old, delta -> (old+delta)})
        latch.countDown()
    }

    fun retrieveJobsDone() : Map<String, Int>{
        return jobsDone
    }

    fun await(seconds: Int): Boolean{
        return latch.await(seconds.toLong(), TimeUnit.SECONDS)
    }
}