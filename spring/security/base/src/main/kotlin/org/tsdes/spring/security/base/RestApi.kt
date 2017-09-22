package org.tsdes.spring.security.base

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 21-Sep-17.
 */
@RestController
class RestApi {

    @GetMapping(produces = arrayOf(MediaType.TEXT_PLAIN_VALUE),
            path = arrayOf("/openToAll"))
    fun openToAll() : String{
        return "openToAll"
    }

    @GetMapping(produces = arrayOf(MediaType.TEXT_PLAIN_VALUE),
            path = arrayOf("/forUsers"))
    fun forUsers() : String{
        return "forUsers"
    }

    @GetMapping(produces = arrayOf(MediaType.TEXT_PLAIN_VALUE),
            path = arrayOf("/forAdmins"))
    fun forAdmins() : String{
        return "forAdmins"
    }

}