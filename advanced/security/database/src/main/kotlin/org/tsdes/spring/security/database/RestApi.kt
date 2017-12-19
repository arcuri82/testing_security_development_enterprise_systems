package org.tsdes.spring.security.database

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.tsdes.spring.security.database.db.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.LinkedHashMap


/**
 * Created by arcuri82 on 08-Nov-17.
 */
@RestController
class RestApi(
        private val service: UserService,
        private val authenticationManager: AuthenticationManager,
        private val userDetailsService: UserDetailsService
) {

    @RequestMapping("/user")
    fun user(user: Authentication): ResponseEntity<Map<String, Any>> {
        /*
            The "Authentication" object (which is a subclass of "Principal")
            contains information of the currently
            authenticated user. SpringBoot will automatically autowire it here.
            If a HTTP request is not authenticated, the "user" here would be
            null. But, anyway, this code would never be reached in the first
            place, as such endpoint is marked as "authenticated()" in
            the WebSecurityConfig file, and so would never be executed if
            no authentication.
         */
        val map = mutableMapOf<String,Any>()
        map.put("name", user.name)
        map.put("roles", AuthorityUtils.authorityListToSet(user.authorities))

        /*
            Returning such data is useful when we want to have different GUIs
            based on the "roles" of the user, eg if s/he is an Admin
         */

        return ResponseEntity.ok(map)
    }


    @PostMapping(path = arrayOf("/signIn"),
            consumes = arrayOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
    fun signIn(@ModelAttribute(name = "the_user") username: String,
               @ModelAttribute(name = "the_password") password: String)
            : ResponseEntity<Void> {

        val registered = service.createUser(username, password, setOf("USER"))

        if (!registered) {
            return ResponseEntity.status(400).build()
        }

        /*
            Not only we created a user, but we also need to authenticate in the
            same request.
            Otherwise, the client would have to do another call with the authentication
            header with the login/password.
            So here, we do the registration manually directly in code, in which
            we need to tell Spring Security the user is authenticated.
         */

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