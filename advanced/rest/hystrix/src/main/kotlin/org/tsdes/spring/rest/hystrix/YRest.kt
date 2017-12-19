package org.tsdes.spring.rest.hystrix

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import rx.Observable
import java.net.URI

/**
 * Created by arcuri82 on 03-Aug-17.
 */
@RestController
@RequestMapping(path = arrayOf("/y"))
class YRest {

    /*
        Note: here for simplicity I am calling another service in the
        same running SpringBoot application (Tomcat server).
        This is just to avoid having to handle two independent
        instances running on two different JVM processes
     */

    private val uri: URI = URI("http://localhost:8080/x")
    private val client: RestTemplate = RestTemplate()


    @GetMapping(path = arrayOf("single"))
    fun doGetSingle(
            @RequestParam("v", defaultValue = "30")
            v: Long): Long {

        /*
            this is synchronous, but would still give an answer within the given time (or
            immediately if the circuit breaker is on)
         */
        val result = CallX(v).execute()
        return result
    }

    @GetMapping(path = arrayOf("multi"))
    fun doGetMulti(
            @RequestParam("a", defaultValue = "30") a: Long,
            @RequestParam("b", defaultValue = "30") b: Long,
            @RequestParam("c", defaultValue = "30") c: Long,
            @RequestParam("d", defaultValue = "30") d: Long,
            @RequestParam("e", defaultValue = "30") e: Long
    ): Long {

        val result = Observable.merge(
                CallX(a).observe(), // make these 5 calls in parallel,
                CallX(b).observe(), // and asynchronously
                CallX(c).observe(),
                CallX(d).observe(),
                CallX(e).observe()
        ).toList().toBlocking().single() // collect the results into a list
                .stream()
                .mapToLong { l -> l }
                .sum() // sum all the returned values in the list

        return result
    }


    /*
        Calls to an external web service will be wrapped into a HystrixCommand
     */
    private inner class CallX(private val x: Long)
        : HystrixCommand<Long>(HystrixCommandGroupKey.Factory.asKey("Interactions with X")) {

        override fun run(): Long {

            /*
                Note: this synchronous call could fail (and so throw an exception),
                or even just taking a long while (if server is under heavy load)
             */

            val result = client.postForEntity(uri, x, Long::class.java)

            return result.body
        }

        override fun getFallback(): Long {
            //this is what is returned in case of exceptions or timeouts
            return 0L
        }
    }
}