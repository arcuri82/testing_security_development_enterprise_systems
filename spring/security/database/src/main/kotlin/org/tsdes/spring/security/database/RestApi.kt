package org.tsdes.spring.security.database

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.spring.security.database.db.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService


/**
 * Created by arcuri82 on 08-Nov-17.
 */
@RestController
class RestApi(
        private val service: UserService,
        private val authenticationManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService
) {

    @PostMapping(path = arrayOf("/signIn"),
            consumes = arrayOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
    fun signIn(@ModelAttribute(name = "the_user") username: String,
               @ModelAttribute(name = "the_password") password: String)
            : ResponseEntity<Void> {

        val registered = service.createUser(username, password, setOf("USER"))

        if (!registered) {
            return ResponseEntity.status(400).build()
        }

        val userDetails = userDetailsService.loadUserByUsername(username)
        val token = UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)

        authenticationManager.authenticate(token)

        if (token.isAuthenticated) {
            SecurityContextHolder.getContext().authentication = token
        }

        return ResponseEntity.status(204).build()
    }

    @GetMapping(produces = arrayOf(MediaType.TEXT_PLAIN_VALUE),
            path = arrayOf("/resource"))
    fun resource(): String {
        return "The Resource"
    }
}