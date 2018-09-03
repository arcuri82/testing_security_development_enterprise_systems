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

        /*
            Here, we do not save on computing the response.
            Still need to use CPU/IO for it.
            However, if detected a match on ETag or Last-Modified,
            then Spring will automatically change response into a 304
            and strip off the body payload.
            This does not save CPU/IO on the server, but reduce the
            size of the response sent over the network.
            This is particularly useful if the payload is large.
         */
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