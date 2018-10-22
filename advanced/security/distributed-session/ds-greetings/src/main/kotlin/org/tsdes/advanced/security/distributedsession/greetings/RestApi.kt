package org.tsdes.advanced.security.distributedsession.greetings

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.advanced.security.distributedsession.userservice.dto.UserInfoDto
import javax.servlet.http.HttpSession


@RestController
class RestApi {

    /*
        This service will communicate with the "user-service" one.
        We inject the ip address here as a variable, as we ll
        change it in the tests.
     */

    @Value("\${userServiceAddress}")
    private lateinit var userServiceAddress: String


    /**
     *   This endpoints make a call to "user-service".
     *   It also gets a boolean parameter as input to determine
     *   whether the call should be authenticated.
     *   Note: this is just an example, as you would always
     *   do an authenticated call.
     */
    @GetMapping(path = ["/api/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getGreeting(
            @PathVariable("id") id: String,
            @RequestParam("ignoreSession", required = false) ignoreSession: Boolean?,
            @CookieValue("SESSION", required = false)
            cookie: String?
    ): ResponseEntity<GreetingsDto> {

        val uri = UriComponentsBuilder
                .fromUriString("http://${userServiceAddress.trim()}/usersInfo/$id")
                .build().toUri()

        val client = RestTemplate()

        val requestHeaders = HttpHeaders()
        if (ignoreSession == null || !ignoreSession) {
            //if we want to be authenticated, we need to make a
            //call with the right cookie
            requestHeaders.add("cookie", "SESSION=$cookie")
        }
        val requestEntity = HttpEntity(null, requestHeaders)


        val response = try {
            client.exchange(uri, HttpMethod.GET, requestEntity, UserInfoDto::class.java)
        } catch (e: HttpStatusCodeException) {
            return if (e.statusCode.value() == 403) {
                /*
                   Note: this is just an example.
                   Using a different code just to make sure
                   to distinguish this case in the tests
                */
                ResponseEntity.status(400).build()
            } else {
                ResponseEntity.status(500).build()
            }
        }

        val payload = response.body

        val dto = GreetingsDto("Hello ${payload.name} ${payload.surname}!!!", payload.email)

        return ResponseEntity.ok(dto)
    }

}

