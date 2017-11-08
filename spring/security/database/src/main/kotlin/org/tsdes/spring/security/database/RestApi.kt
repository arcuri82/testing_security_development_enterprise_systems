package org.tsdes.spring.security.database

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.spring.security.database.db.UserService

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@RestController
class RestApi(
        private val service: UserService
) {

    @PostMapping(path = arrayOf("/signIn"),
            consumes = arrayOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
    fun signIn(@ModelAttribute(name = "the_user") username: String,
               @ModelAttribute(name = "the_password") password: String)
            : ResponseEntity<Void> {

        val registered = service.createUser(username, password, setOf("USER"))

        if (registered) {
            //TODO autologin
            return ResponseEntity.status(204).build()
        } else {
            return ResponseEntity.status(400).build()
        }
    }

    @GetMapping(produces = arrayOf(MediaType.TEXT_PLAIN_VALUE),
            path = arrayOf("/resource"))
    fun resource(): String {
        return "The Resource"
    }
}