package org.tsdes.spring.amqp.distributedwork

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Singleton that I am using to keep track of how
 * often a specific worker complete a job.
 *
 * Created by arcuri82 on 07-Aug-17.
 */
class Counter {

    /**
        key -> worker id
        value -> jobs done by the given worker
     */
    private val jobsDone: MutableMap<String, Int> = ConcurrentHashMap()

    /*
        Latch used to wait until a certain number of jobs have
        been completed
     */
    private var latch: CountDownLatch = CountDownLatch(0)

    fun reset(n: Int) {
        jobsDone.clear()
        latch = CountDownLatch(n)
    }

    fun doneJob(id: String) {
        /*
            Merge: if "id" is not present, value becomes 1.
            If present, existing value is increased by 1.
         */
        jobsDone.merge(id, 1) { old, delta -> (old + delta) }
        latch.countDown()
    }

    fun retrieveJobsDone(): Map<String, Int> {
        return jobsDone
    }

    /**
     * Await until N jobs are completed
     */
    fun await(seconds: Int): Boolean {
        return latch.await(seconds.toLong(), TimeUnit.SECONDS)
    }
}