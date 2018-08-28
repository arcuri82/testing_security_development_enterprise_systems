package org.tsdes.advanced.rest.conditionalget

import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by arcuri82 on 27-Aug-18.
 */
@Component
class NewsRepository {

    private val queue: Queue<String> = ConcurrentLinkedDeque<String>()

    private val max = 10

    /*
     * This might seem bit weird... we have a property, for which we
     * want the setter to be used only in this class.
     * But compiler will complain, because private setters are only allowed
     * for final properties that cannot be overridden.
     * In Kotlin, everything is final by default, but, to be able to use Spring,
     * we needed the "open-all" Maven plugin.
     * So, here, we need to explicitly mark it as "final", although it does NOT
     * mean that its value cannot be changed (like it would be with "val").
     * It just means that it cannot be overridden in a subclass.
     */
    final var time: ZonedDateTime = ZonedDateTime.now()
        private set

    private val counter: AtomicInteger = AtomicInteger(0)


    fun reset(){
        queue.clear()
        counter.set(0)
        time = ZonedDateTime.now()
    }

    /**
     * An ETAG could be anything, as long as it is unique. In this particular
     * case, we keep track of each modification in a counter.
     * Another option is to compute a hash on the HTTP responses.
     * This latter is more expensive, but way more general.
     * Spring has some automated support for it, eg see ShallowEtagHeaderFilter
     */
    final val etag: String
        get() = counter.toString()


    fun getCurrent() : List<String>{
        return queue.toList()
    }

    fun addNews(news: String){

        synchronized(this){
            time = ZonedDateTime.now()
            counter.getAndIncrement()
            queue.add(news)
            if(queue.size > max){
                queue.remove()
            }
        }
    }
}