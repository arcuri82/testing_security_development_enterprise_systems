package org.tsdes.advanced.rest.cache

import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

/**
 * Created by arcuri82 on 30-Aug-18.
 */
@RestController
@RequestMapping(path = ["/x"])
class XRest(
        //we inject bean, so we get the configurations for cache define
        //in entry-point of the app
        val restTemplate: RestTemplate
) {

    var port = 8080

    @GetMapping
    fun get() : ResponseEntity<Int>{

        val res = restTemplate.getForEntity("http://localhost:$port/y", Int::class.java)

        return ResponseEntity.ok(res.body)
    }
}