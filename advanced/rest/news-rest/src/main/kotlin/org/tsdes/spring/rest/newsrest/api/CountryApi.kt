package org.tsdes.spring.rest.newsrest.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.tsdes.spring.examples.news.constraint.CountryList


/**
 * Created by arcuri82 on 06-Jul-17.
 */
@Api(value = "/countries", description = "API for country data.")
@RestController
class CountryApi {

    @ApiOperation("Retrieve list of country names")
    @GetMapping(
            path = arrayOf("/countries"),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun get() : ResponseEntity<List<String>> {

        return ResponseEntity.ok(CountryList.countries)
    }
}