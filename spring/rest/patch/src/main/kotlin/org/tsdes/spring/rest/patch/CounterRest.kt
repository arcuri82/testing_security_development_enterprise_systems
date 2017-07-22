package org.tsdes.spring.rest.patch

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

/**
 * Created by arcuri82 on 20-Jul-17.
 */
@Api("Handle named, numeric counters")
@RequestMapping(path = arrayOf("/counters"))
@RestController
class CounterRest {

    private val idGenerator = AtomicLong(0)

    /**
     * Map for the dtos, where the key is the dto.id
     */
    private val map: MutableMap<Long, CounterDto> = ConcurrentHashMap()


    @ApiOperation("Create a new counter")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun create(@RequestBody dto: CounterDto): ResponseEntity<Void> {

        if (dto.id == null) {
            dto.id = idGenerator.getAndIncrement()
        } else if (map.containsKey(dto.id!!)) {
            //Should not create a new counter with id that already exists
            return ResponseEntity.status(400).build()
        }

        if (dto.value == null) {
            dto.value = 0
        }

        map.put(dto.id!!, dto)

        /*
            here created will set the "Location" header, by specifying the URI of where we can GET it from.
            A relative (ie not absolute) URI will resolve against the URI of the request, ie "/patch/api/counters".
            Note that the response here has no body.
         */
        return ResponseEntity.created(URI.create("counters/" + dto.id)).build()
    }


    @ApiOperation("Get all the existing counters")
    @GetMapping(produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getAll(): List<CounterDto> {
        return map.values.toList()
    }


    @ApiOperation("Return the counter with the given id")
    @GetMapping(path = arrayOf("/{id}"),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getById(
            @ApiParam("The unique id of the counter")
            @PathVariable("id")
            id: Long): ResponseEntity<CounterDto> {

        val dto = map[id]
                ?: return ResponseEntity.status(404).build() // No counter with id exists

        return ResponseEntity.ok(dto)
    }

    /*
        A PUT does completely replace the resource with a new one
     */

    @ApiOperation("Replace a counter")
    @PutMapping(path = arrayOf("/{id}"), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun update(@ApiParam("The unique id of the counter")
               @PathVariable("id")
               id: Long,
            //
               @ApiParam("The new state of the resource")
               @RequestBody
               dto: CounterDto): ResponseEntity<Void> {

        if (dto.id != id) {
            //Should not have inconsistent id
            return ResponseEntity.status(409).build()
        }

        if (dto.value == null) {
            dto.value = 0
        }

        /*
            Note: here we are allowing PUT to create a new resource
         */
        val code = if (map.containsKey(id)) 204 else 201

        map.put(id, dto)

        return ResponseEntity.status(code).build()
    }


    /*
        A PATCH does a partial update of a resource.
        However, there is no strict rule on how the resource should be
        updated.
        The client has to send "instructions" in a format that the server
        can recognize and execute.
        Can also be a custom format, like in this case.
     */

    @ApiOperation("Modify the counter based on the instructions in the request body")
    @PatchMapping(path = arrayOf("/{id}"),
            // could have had a custom type here, but then would need unmarshaller for it
            consumes = arrayOf(MediaType.TEXT_PLAIN_VALUE))
    fun patch(@ApiParam("The unique id of the counter")
              @PathVariable("id")
              id: Long,
            //
              @ApiParam("The instructions on how to modify the counter. " +
                      "In this specific case, it should be a single numeric value " +
                      "representing a delta that " +
                      "will be applied on the counter value")
              text: String)
            : ResponseEntity<Void> {

        val dto = map[id]
                ?: return ResponseEntity.status(404).build()

        val delta = try {
            text.toInt()
        } catch (e: NumberFormatException) {
            //Invalid instructions. Should contain just a number
            return ResponseEntity.status(400).build()
        }

        dto.value = dto.value!! + delta

        return ResponseEntity.status(204).build()
    }

}