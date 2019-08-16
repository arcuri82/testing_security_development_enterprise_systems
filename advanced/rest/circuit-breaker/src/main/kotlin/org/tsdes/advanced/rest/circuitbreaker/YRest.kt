package org.tsdes.advanced.rest.circuitbreaker

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI

/**
 * Created by arcuri82 on 03-Aug-17.
 */
@RestController
class YRest {

    /*
        Note: here for simplicity I am calling another service in the
        same running SpringBoot application (Tomcat server).
        This is just to avoid having to handle two independent
        instances running on two different JVM processes
     */

    var port = 8080

    private val client: RestTemplate = RestTemplate()


    @GetMapping(path = ["/y"])
    fun doGetSingle(
            @RequestParam("v")
            v: Long
    ): Long {

        /*
            this is synchronous, but would still give an answer within the given time (or
            immediately if the circuit breaker is on)
         */
        return CallX(v).execute()
    }


    /*
        TODO: note that Hystrix is now no longer in active development, although it is maintained.
        https://github.com/Netflix/Hystrix
        Once Spring Cloud Circuit Breaker is out of incubation phase, it should be replaced here
        https://spring.io/blog/2019/04/16/introducing-spring-cloud-circuit-breaker
     */

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

            val uri = URI("http://localhost:$port/x")

            val result = client.postForEntity(uri, x, Long::class.java)

            return result.body
        }

        override fun getFallback(): Long {
            //this is what is returned in case of exceptions or timeouts
            return 0L
        }
    }
}