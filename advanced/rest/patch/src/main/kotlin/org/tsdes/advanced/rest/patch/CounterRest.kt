package org.tsdes.advanced.rest.patch

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by arcuri82 on 20-Jul-17.
 */
@Api("Handle named, numeric counters")
@RequestMapping(path = ["/patch/api/counters"])
@RestController
class CounterRest {

    /**
     * Used to create unique ids.
     * Note the fact that this is atomic to avoid
     * concurrency issues if serving 2 HTTP requests
     * at same time in different threads
     */
    private val idGenerator = AtomicLong(0)

    /**
     * Map for the dtos, where the key is the dto.id
     */
    private val map: MutableMap<Long, CounterDto> = ConcurrentHashMap()

    /*
        WARNING: the above is "internal" state of the API.
        Usually, this is a "a bad thing", and should be avoided.
        But here we have it just to make the example simpler,
        so do not have to deal with databases.
        We ll go back to this point when we will speak of
        "scaling horizontally" in microservices.
     */

    @ApiOperation("Create a new counter")
    @PostMapping(consumes = [(MediaType.APPLICATION_JSON_VALUE)])
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
            A relative (ie not absolute) URI will resolve against the URI of the request, eg "http://localhost:8080".
            Note that the response here has no body.

            The return status of "created()" is 201
         */
        return ResponseEntity.created(URI.create("/patch/api/counters/" + dto.id)).build()
    }


    @ApiOperation("Get all the existing counters")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getAll(): Collection<CounterDto> {
        return map.values
    }


    @ApiOperation("Return the counter with the given id")
    @GetMapping(path = ["/{id}"],
            produces = [(MediaType.APPLICATION_JSON_VALUE)])
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
    @PutMapping(path = ["/{id}"], consumes = [(MediaType.APPLICATION_JSON_VALUE)])
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

            Here, I am allowing creating resources with PUT.
            Using 201 (created) to mark this event instead of
            a generic 204 (OK, but no content to return).

            Also note that this code leads to a BUG, as IDs could clash
            with the ones used afterwards when we create a new resource with POST,
            ie we must guarantee that POST will pick unused ids.
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
    @PatchMapping(path = ["/{id}"],
            // could have had a custom type here, but then would need an unmarshaller for it
            consumes = [(MediaType.TEXT_PLAIN_VALUE)])
    fun patch(@ApiParam("The unique id of the counter")
              @PathVariable("id")
              id: Long,
            //
              @ApiParam("The instructions on how to modify the counter. " +
                      "In this specific case, it should be a single numeric value " +
                      "representing a delta that " +
                      "will be applied on the counter value")
              @RequestBody
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


    /*
        When dealing with resources that can be expressed/modelled with JSON,
        there are two main formats for instructions:
        - (1) JSON Patch
        - (2) JSON Merge Patch

        (1) provide a list of operations (eg, add, remove, move, test).
        It is more expressive than (2), but
        at the same time more complicated to handle.

        (2) is just sending a subset of the JSON, and each of specified
        changes will be applied, ie overwritten.
        The only tricky thing to keep in mind is the handling of "null"
        values. Missing/unspecified elements will be ignored, whereas
        elements with null will get null.
        For example, if you have:
        {"A":... , "B":...} as JSON resource, and you get a patch with:
        {"B":null}, then A will not be modified, whereas B will be removed,
        ie results of the resource would be:
        {"A":...}
        without the B.
        Note: in JSON there is a difference between a missing element and
        and an element with null, ie
        {"A":...} and
        {"A":... , "B":null}
        are technically not the same.

        However, as we will deal with Kotlin objects, we can safely "ignore"
        this distinction in our model: ie make no difference between a
        missing element and an element with null value.

        The only catch is in the parsing of the PATCH JSON Merge objects.
        We CANNOT unmarshal it to a DTO, as we would have no way to distinguish
        between missing elements and actual null values.
     */

    @ApiOperation("Modify the counter using JSON Merge Patch")
    @PatchMapping(path = ["/{id}"],
            consumes = ["application/merge-patch+json"])
    fun mergePatch(@ApiParam("The unique id of the counter")
                   @PathVariable("id")
                   id: Long?,
                   @ApiParam("The partial patch")
                   @RequestBody
                   jsonPatch: String)
            : ResponseEntity<Void> {

        val dto = map[id]
                ?: return ResponseEntity.status(404).build()

        val jackson = ObjectMapper()

        val jsonNode: JsonNode
        try {
            jsonNode = jackson.readValue(jsonPatch, JsonNode::class.java)
        } catch (e: Exception) {
            //Invalid JSON data as input
            return ResponseEntity.status(400).build()
        }

        if (jsonNode.has("id")) {
            //shouldn't be allowed to modify the counter id
            return ResponseEntity.status(409).build()
        }

        //do not alter dto till all data is validated. A PATCH has to be atomic,
        //either all modifications are done, or none.
        var newName = dto.name
        var newValue = dto.value

        if (jsonNode.has("name")) {
            val nameNode = jsonNode.get("name")
            if (nameNode.isNull) {
                newName = null
            } else if (nameNode.isTextual) {
                newName = nameNode.asText()
            } else {
                //Invalid JSON. Non-string name
                return ResponseEntity.status(400).build()
            }

            //Doing this here would be wrong!!!
            //eg, what happened if "value" parsing fail? we ll have side-effects
            //
            //dto.name = newName;
        }

        if (jsonNode.has("value")) {

            val valueNode = jsonNode.get("value")

            if (valueNode.isNull) {
                newValue = 0
            } else if (valueNode.isNumber) {
                //note: if this is not numeric, it silently returns 0...
                newValue = valueNode.intValue()
            } else {
                //Invalid JSON. Non-numeric value
                return ResponseEntity.status(400).build()
            }
        }

        //now that the input is validated, do the update
        dto.name = newName
        dto.value = newValue

        return ResponseEntity.status(204).build()
    }
}