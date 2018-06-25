package org.tsdes.spring.security.basic

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 21-Sep-17.
 */
@RestController
class RestApi {

    @GetMapping(produces = [(MediaType.TEXT_PLAIN_VALUE)],
            path = ["/openToAll"])
    fun openToAll() : String{
        return "openToAll"
    }

    @GetMapping(produces = [(MediaType.TEXT_PLAIN_VALUE)],
            path = ["/forUsers"])
    fun forUsers() : String{
        return "forUsers"
    }

    @GetMapping(produces = [(MediaType.TEXT_PLAIN_VALUE)],
            path = ["/forAdmins"])
    fun forAdmins() : String{
        return "forAdmins"
    }

}