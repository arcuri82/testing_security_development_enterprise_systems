package org.tsdes.advanced.rest.wiremock

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

/**
 * Created by arcuri82 on 02-Aug-17.
 */
@RestController
@RequestMapping(path = arrayOf("/convert"))
class ConverterRestServiceXml {

    /*
        @Value us used to inject properties from the "application.properties" file.
        Note, however, the "\" in front of "$".
        Spring uses "${}" to resolve properties.
        However, Kotlin uses "${}" for string interpolation.
        So, to bypass the interpolation, we need to escape the "$"

        This API will make a connection to an external one.
        The URL of this external service is injected, and not hardcoded
        here. The reason is that, being a property, we can easily change it
        based on the context.
        For example, during testing, we will not connect to the real external
        server, but rather change the address to point to a mock one running
        on the local host.
     */
    @Value("\${fixerWebAddress}")
    private lateinit var webAddress: String


    @GetMapping(produces = arrayOf(MediaType.APPLICATION_XML_VALUE))
    fun getExchangeRate(@RequestParam("from") __from: String,
                        @RequestParam("to") __to: String)
            : ResponseEntity<ConversionDto> {

        /*
            Unfortunately, in Kotlin input parameters are final (ie "val").
            So, we cannot modify them, and we need to create new ones.
            This is an issue when we need new names for variables that
            should mean the same
         */
        val from = __from.toUpperCase()
        val to = __to.toUpperCase()

        /*
            call an external web service to get the rate.
            Example:  http://api.fixer.io/latest?base=NOK
        */
        val uri = UriComponentsBuilder
                .fromUriString("http://${webAddress.trim()}/latest")
                .queryParam("base", from)
                .build().toUri()

        val client = RestTemplate()

        /*
            Here the choice of error family (4xx or 5xx?) is tricky.

            If user sends wrong inputs, then of course we return a 4xx.
            But what if the external service (http://api.fixer.io in this case)
            is down? It is neither our fault (I mean this Rest service), nor of the
            client user. Still, we need to return a 5xx, as the request was correct
            (so no 4xx), but the service could not fulfil it.

             So, in general, a 5xx could mean either:
             (1) a bug in the service
             (2) service depends on external services (or database) that are
                 not working properly.
         */

        val response = try {
            client.getForEntity(uri, String::class.java)
        } catch (e: HttpClientErrorException) {
            /*
                Note: the (current) design of Spring's RestTemplate library
                is quite arguable...
                For example, what would happen if above call results in a 404?
                You could expect that you could check such status code in the
                response object, ie "response.statusCode". Well, that does
                not really work, as RestTemplate decides to throw an exception !?!

                It could be better to use a client library like an implementation
                of the specs JAX-RS (eg, RestEasy), but mixing it together with
                a Spring application might lead to some headaches...

                Here 400 could be due to validation of parameters, which is
                likely a user error. But other issues (eg 404) might be
                a misconfiguration in this application (eg, wrong URL), and
                so a bug (500)
             */
            val code = if (e.statusCode.value() == 400) 400 else 500
            return ResponseEntity.status(code).build()
        }

        if (response.statusCode.run { is4xxClientError || is5xxServerError }) {
            val code = if (response.statusCode.value() == 400) 400 else 500
            return ResponseEntity.status(code).build()
        }

        val jackson = ObjectMapper()

        val rootNode: JsonNode
        try {
            rootNode = jackson.readValue(response.body, JsonNode::class.java)
        } catch (e: Exception) {
            //Invalid JSON data as input
            return ResponseEntity.status(500).build()
        }

        val base = rootNode.get("base").asText()

        if (from != base || !rootNode.has("rates")) {
            //internal error
            return ResponseEntity.status(500).build()
        }

        if (!rootNode.get("rates").has(to)) {
            //Non-recognized
            return ResponseEntity.status(400).build()
        }

        val rate = rootNode.get("rates").get(to).asDouble()

        //convert such "rate" into our own dto format
        val dto = ConversionDto(from, to, rate)

        return ResponseEntity.ok(dto)
    }
}