package org.tsdes.spring.rest.newsrest.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.tsdes.spring.examples.news.constraint.CountryList
import javax.ws.rs.core.MediaType

/**
 * Created by arcuri82 on 06-Jul-17.
 */
@Api(value = "/countries", description = "API for country data.")
@RestController
class CountryApi {

    @ApiOperation("Retrieve list of country names")
    @RequestMapping(
            path = arrayOf("/countries"),
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf(MediaType.APPLICATION_JSON))
    fun get() : ResponseEntity<List<String>> {

        return ResponseEntity.ok(CountryList.countries)
    }
}