package org.tsdes.advanced.rest.cache

import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

/**
 * Created by arcuri82 on 30-Aug-18.
 */
@RestController
@RequestMapping(path = ["/y"])
class YRest {

    var howOftenCalled: Int = 0

    val theValue = 42

    @GetMapping
    fun get() : ResponseEntity<Int>{

        /*
            Here we just want to simulate some resource that should be valid
            for a certain amount of time.
            Eg, let's say we have a REST for Weather Forecast.
            We have some other tool that populate the database based on the
            computed forecast, and that maybe is done every second hour.
            So, each time we give out the forecast, we can give it a time-to-live
            equal of the time left till the next re-computation of the resource.
         */

        howOftenCalled++

        return ResponseEntity
                .status(200)
                //for simplicity, here we state that for sure this resource is valid
                //for at least 2 seconds, and can be stored also on public caches
                .cacheControl(CacheControl.maxAge(2, TimeUnit.SECONDS).cachePublic())
                //we do not care of actual value in this example
                .body(theValue)
    }
}