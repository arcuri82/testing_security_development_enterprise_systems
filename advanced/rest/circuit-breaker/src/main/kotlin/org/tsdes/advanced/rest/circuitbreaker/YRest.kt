package org.tsdes.advanced.rest.circuitbreaker


import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI
import javax.annotation.PostConstruct

/**
 * Created by arcuri82 on 03-Aug-17.
 */
@RestController
class YRest(
        private val circuitBreakerFactory : Resilience4JCircuitBreakerFactory
) {

    /*
        Note: here for simplicity I am calling another service in the
        same running SpringBoot application (Tomcat server).
        This is just to avoid having to handle two independent
        instances running on two different JVM processes
     */

    var port = 8080

    private val client: RestTemplate = RestTemplate()

    private  var cb : CircuitBreaker? =null

    @PostConstruct
    fun init(){
        //FIXME: unfortunately this does not work, as factory not initialized yet :(
        //cb = circuitBreakerFactory.create("circuitBreakerToX")
    }

    @GetMapping(path = ["/y"])
    fun doGetSingle(
            @RequestParam("v")
            v: Long
    ): Long {

        if (cb == null)
            cb = circuitBreakerFactory.create("circuitBreakerToX")

        /*
            this is synchronous, but would still give an answer within the given time (or
            immediately if the circuit breaker is on)
         */

        return cb!!.run(
                //the command, doing the HTTP call
                {
                    val uri = URI("http://localhost:$port/x")
                    val result = client.postForEntity(uri, v, Long::class.java)
                    result.body
                },
                //this is what is returned in case of exceptions or timeouts
                { e -> 0L }
        )
    }
}