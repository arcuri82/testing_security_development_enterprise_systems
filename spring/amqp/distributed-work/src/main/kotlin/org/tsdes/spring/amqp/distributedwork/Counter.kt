package org.tsdes.spring.amqp.distributedwork

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
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

    fun doneJob(id: String){
        jobsDone.merge(id, 1, { old, delta -> (old+delta)})
        latch.countDown()
    }

    fun retrieveJobsDone() : Map<String, Int>{
        return jobsDone
    }

    fun await(seconds: Int): Boolean{
        return latch.await(seconds.toLong(), TimeUnit.SECONDS)
    }
}