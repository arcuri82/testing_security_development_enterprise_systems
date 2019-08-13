package org.tsdes.advanced.rest.redirect

import io.swagger.annotations.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.spring.rest.patch.CounterDto
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


/*
    The specs about the redirect 3xx codes are quite "unclear",
    to say the least...
    A very good resource to better understanding 3xx is:

    http://insanecoding.blogspot.no/2014/02/http-308-incompetence-expected.html

    The problem stems from the fact of how browsers handled 3xx codes,
    and at times it was in a very wrong way, which did impact the writing of the standards.
    However, here we do not deal directly with browsers (eg, submissions of HTML forms),
    but clients of a RESTful API (including AJAX calls in browsers),
    where we also provide documentation with Swagger on how to use it.

    To summarize:

    300: do NOT use, unless you are implementing a HATEOAS API.

    301: permanent redirect. Goal: when redirecting from X to Y, this means that we are telling
         the client that X is not a valid URI anymore, and so should always use Y from now on.
         However, the specs allow changing verb, which does not make any sense.
         E.g, do not get surprised if some libraries/tools redirect a DELETE into
         a GET (!?!).
         This should be used for permanent redirect of GETs, but, for the other
         methods like POST, see 308.


    302: do NOT use. Do use 307 instead. However, most browsers interpret 302 like
         it was a 303.


    303: do NOT use it, although it is not a mistake. For example, the return code of
         a POST could be a 303 pointing to the newly create resource, which will be
         accessed with a GET. A 303 means that the action initiated by the request is
         completed, and the redirect will point to a different resource location.
         This is useful in browsers: for example, when doing a POST from a text form,
         the server might want to redirect to a new page with the results of the POST,
         and to do that the way is to use 303 as response code.
         But here we do not deal directly with browser, so a 201 (Created) could be
         be better, but still with a valid Location header.
         The client of the API will then decide what to do with it.

    304: needed when dealing with caches. It means that a resource has not been
         changed since last request, so the copy in cache can used without the
         server needing to resend the payload.

    307: temporary redirect, using the same verb (see discussion about 301).
         Usage: needed when a resource location can change several times.
         Example: latest news, this might vary each hour. You might want
         to a have a fixed URI resource for "latest" that does a temporarily
         redirect to the current latest news.


    308: permanent redirect, using the same verb.
         However, it is a complex story:
         - added afterwards (2015), and not so widely supported.
         - should be like 301 with no verb change allowed,
           but many internet entities (eg Google) use this
           code as a non-standard code with a completely different meaning,
           ie "Resume Incomplete"
         - apart from POST -> GET with a 302, might not be possible to have redirection
           but for GET. If you try to use a 301 redirection with POST/PUT/DELETE/PATCH,
           expect all kinds of weird behaviors from your clients if the
           re-directions are followed automatically.
         - for POST/PUT/DELETE/PATCH could still use 308 for documentation, even
           if many libraries will not handle it (not handling 308 is still better
           than screwing you up by changing verb into a GET)
         - for backward compatibility and wide support, for GET do not use 308, but
           rather 301
 */

/**
 * Created by arcuri82 on 31-Jul-17.
 */
@Api("Handle named, numeric counters")
@RequestMapping(path = ["/redirect/api/counters"])
@RestController
class CounterRest {


    private val idGenerator = AtomicLong(0)

    /**
     * Map for the dtos, where the key is the dto.id
     */
    private val map: MutableMap<Long, CounterDto> = ConcurrentHashMap()

    /**
     * Keep track of the latest created counter
     */
    private var latest: CounterDto? = null


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

        latest = dto

        return ResponseEntity.created(URI.create("/redirect/api/counters/" + dto.id)).build()
    }

    @ApiOperation("Delete all existing counters.")
    @DeleteMapping
    fun deleteAllCounters() : ResponseEntity<Void>{
        latest = null
        map.clear()

        return  ResponseEntity.status(204).build()
    }


    @ApiOperation("Get all the existing counters")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun getAll(): Collection<CounterDto> {
        return map.values
    }


    /*
        Note: following endpoints are not really "best practice",
        as "/id/{id}" should rather be "/{id}", and "theLatest"/"latest"
        would rather be filtering options.
        But here we use them like this just to simplify the examples for 3xx.
     */

    @ApiOperation("Return the counter with the given id")
    @GetMapping(path = ["/id/{id}"])
    fun get(
            @ApiParam("The unique id of the counter")
            @PathVariable("id")
            id: Long?): ResponseEntity<CounterDto> {

        val dto = map[id]
                ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(dto)
    }

    /*
        Lot of good reasons to do a permanent redirection.
        A typical case is when the domain of the server changes, eg
        from "www.foo.com" to "www.bar.com".
        However, in that case the redirect should be handled by the
        general config of the server.

        Another good case is when a resource location is deprecated,
        eg when it is renamed.
        You might want to not delete it yet, as to avoid breaking current
        clients that have not updated yet.
     */
    @ApiOperation("Deprecated. Use \"latest\" instead")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @GetMapping(path = ["/theLatest"])
    @Deprecated("", ReplaceWith(""))
    fun theLatest(): ResponseEntity<Void> {

        return ResponseEntity.status(301)
                .location(URI.create("/redirect/api/counters/latest"))
                .build()
    }


    /*
        Here, instead of returning the latest counter, we do a
        redirect to its location.

        This is slightly more inefficient (client has to do 2 different
        HTTP GET calls), but has these advantages:
        - make clear that the resource can change often
        - the resource might be on a different web service
        - allows to do other operations on the resource, eg DELETE/PUT/PATCH, as
          we get the URI of such resource
     */

    @ApiOperation("Find the most recently created counter")
    @ApiResponses(ApiResponse(code = 307, message = "Temporary redirect."), ApiResponse(code = 409, message = "There is no counter"))
    @GetMapping(path = ["/latest"])
    fun latest(): ResponseEntity<Void> {

        if (latest == null) {
            /*
                Note: tricky code to choose. Why not 404?
                Point is, if used 404, could not distinguish if
                the client is querying a wrong URI (eg a misspelled "lates")
             */
            return ResponseEntity.status(409).build()
        }

        //temporary redirect
        return ResponseEntity.status(307)
                .location(URI.create("/redirect/api/counters/id/" + latest!!.id))
                .build()
    }

}