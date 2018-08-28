package org.tsdes.advanced.rest.conditionalget

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Created by arcuri82 on 27-Aug-18.
 */
@Api("News API")
@RequestMapping(path = ["/conditionalNews/api/news"])
@RestController
class ConditionalGetRest(
        val repository: NewsRepository
) {

    @ApiOperation("Get all the recent news")
    @GetMapping
    fun getAll() : ResponseEntity<List<String>>{

        return ResponseEntity
                .status(200)
                .lastModified(repository.time.toInstant().toEpochMilli())
                .eTag(repository.etag)
                .body(repository.getCurrent())
    }

    @ApiOperation("Create a news")
    @PostMapping
    fun createNews(@RequestBody news: String) : ResponseEntity<Void>{

        repository.addNews(news)

        return ResponseEntity.status(201).build()
    }
}