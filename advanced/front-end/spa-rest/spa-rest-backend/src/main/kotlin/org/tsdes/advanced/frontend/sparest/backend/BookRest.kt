package org.tsdes.advanced.frontend.sparest.backend

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Api(value = "/books", description = "Handling of creating and retrieving book entries")
@RequestMapping(path = ["/books"])
@RestController
class BookRest {



//    @ApiOperation("Get all the books")
//    @GetMapping
//    fun get(): ResponseEntity<List<>> {
//
//    }

}