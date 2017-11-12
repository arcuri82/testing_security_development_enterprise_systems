package org.tsdes.spring.security.distributedsession.greetings

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpSession


@RestController
class RestApi {

    @Value("\${userServiceAddress}")
    private lateinit var userServiceAddress: String


    @GetMapping(
            path = arrayOf("/api/{id}"),
            produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE)
    )
    fun getGreeting(
            @PathVariable("id") id: String,
            @RequestParam("ignoreSession", required = false) ignoreSession: Boolean?,
            session: HttpSession
    ): ResponseEntity<GreetingsDto> {

        val uri = UriComponentsBuilder
                .fromUriString("http://${userServiceAddress.trim()}/usersInfo/$id")
                .build().toUri()

        val client = RestTemplate()

        val requestHeaders = HttpHeaders()
        if (ignoreSession == null || !ignoreSession) {
            requestHeaders.add("cookie", "SESSION=${session.id}")
        }
        val requestEntity = HttpEntity(null, requestHeaders)



        val response = try {
            client.exchange(uri, HttpMethod.GET, requestEntity, String::class.java)
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

        if (response.statusCode.value() == 403) {

        }

        /*
            If we put the DTOs of UserService in their own module, we
            could use them instead of unmarshalling code manually
         */
        val jackson = ObjectMapper()

        val rootNode = jackson.readValue(response.body, JsonNode::class.java)

        val name = rootNode.get("name").asText()
        val surname = rootNode.get("surname").asText()
        val email = rootNode.get("email").asText()

        val dto = GreetingsDto("Hello $name $surname!!!", email)

        return ResponseEntity.ok(dto)
    }

}

