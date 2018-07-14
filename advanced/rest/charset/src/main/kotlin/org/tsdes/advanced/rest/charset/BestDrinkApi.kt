package org.tsdes.advanced.rest.charset

import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

/**
 * Created by arcuri82 on 18-Jul-17.
 */
@RestController
class BestDrinkApi {

    @ApiOperation("Get the name of the best drink")
    @GetMapping(
            path = ["/charset/drinks/best"],
            // provide the resource as String in two different charset encodings
            produces = ["text/plain;charset=UTF-8", "text/plain;charset=ISO-8859-1"]
    )
    fun getTheBest(@RequestHeader("Accept-Language", required = false)
                   languages: String?
    ): String {

        if (languages != null && languages.trim().equals("no", ignoreCase = true)) {
            /*
                actually, in the Accept-Language there can be a list of
                acceptable languages, and would need to parse it.
                but here for this example, it is not so important.
                see the specs if you want to see the details on how
                such string should be parsed
             */
            return "Øl"
        } else {
            return "Beer"
        }
    }
}