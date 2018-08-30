package org.tsdes.advanced.rest.conditionalchange

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime

/**
 * Created by arcuri82 on 28-Aug-18.
 */
@Api("Counter API")
@RequestMapping(path = ["/conditionalCounter/api/counter"])
@RestController
@Validated
class CounterRest {

    @Volatile
    private var counter = 0

    @Volatile
    private var name = "default"

    @Volatile
    private var time = ZonedDateTime.now()

    @ApiOperation("Get the counter")
    @GetMapping
    fun get(): ResponseEntity<CounterDto> {

        /*
            If this was saved on a database, could use the unique primary
            key as etag. here, i just need something unique.
            if this would be too long, then one option could to hash it to
            a fixed size string (eg, using MD5).
            This does not guarantee uniqueness, but a clash would be extremely
            unlikely.
         */
        val etag = computeETag()

        return ResponseEntity
                .status(200)
                .eTag(etag)
                .lastModified(time.toInstant().toEpochMilli())
                .body(CounterDto(name, counter))
    }

    private fun computeETag() = "$name:$counter"

    @ApiOperation("Change the counter")
    @PutMapping
    fun update(
            @RequestHeader("If-Match") ifMatch: String?,
            @RequestBody dto: CounterDto
    ): ResponseEntity<Void> {

        if (ifMatch != null && ifMatch.trim() != computeETag()) {
            return ResponseEntity.status(412).build()
        }

        synchronized(this) {
            this.counter = dto.value!!
            this.name = dto.name!!

            this.time = ZonedDateTime.now()
        }

        return ResponseEntity.status(204).build()
    }
}