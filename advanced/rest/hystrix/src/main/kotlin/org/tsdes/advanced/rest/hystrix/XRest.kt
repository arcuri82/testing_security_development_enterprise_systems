package org.tsdes.advanced.rest.hystrix

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 03-Aug-17.
 */
@RestController
class XRest {

    @PostMapping(path = ["/x"])
    fun doProcess(@RequestBody x: Long): Long {

        /*
            simulate some delay in the response, eg due to network congestion
            or hardware/software issues of the server
         */
        Thread.sleep(x)

        return x * 2
    }
}