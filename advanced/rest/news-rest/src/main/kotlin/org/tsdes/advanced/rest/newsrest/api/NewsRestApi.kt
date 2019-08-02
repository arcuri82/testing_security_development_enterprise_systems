package org.tsdes.advanced.rest.newsrest.api

import com.google.common.base.Throwables
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.examplenews.constraint.Country
import org.tsdes.advanced.rest.newsrest.dto.NewsConverter
import org.tsdes.advanced.rest.newsrest.dto.NewsDto
import javax.validation.ConstraintViolationException
import javax.validation.Valid

const val ID_PARAM = "The numeric id of the news"

/**
 * Created by arcuri82 on 13-Jul-17.
 */
@Api(value = "/news", description = "Handling of creating and retrieving news")
@RequestMapping(
        path = ["/news"] // when the url is "<base>/news", then this class will be used to handle it
)
@RestController
class NewsRestApi {

    /*
        Main HTTP verbs/methods:
        GET: get the resource specified in the URL
        POST: send data, creating a new resource
        PUT: update a resource
        PATCH: partial update
        DELETE: delete the resource

        Common status codes:
        200: OK
        201: Resource created
        204: Done, nothing to return
        400: General user error, bad request
        404: Resource not found
        409: Conflict with current state
        500: Server error
     */

    /*
        Note: here inputs (what is in the method parameters) and outputs will
        be automatically processed by Spring using its own JSON library (eg Jackson).
        So, when we have
        "List<NewsDto>"
        as return value, Spring will automatically marshall it into JSON
     */


    @Autowired
    private lateinit var crud: NewsRepository

        /*
            request URL parameters are in the form

            ?<name>=<value>&<name>=<value>&...

            for example

            /news?country=Norway&authordId=foo

            So here we ll have a single endpoint for getting "news", where
            optional filtering on "country" and "authorId" will be based on
            URL parameters, and not different endpoints
         */
    @ApiOperation("Get all the news")
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)])
    fun get(@ApiParam("The country name")
            @RequestParam("country", required = false)
            country: String?,
            //
            @ApiParam("The id of the author who wrote the news")
            @RequestParam("authorId", required = false)
            authorId: String?

    ): ResponseEntity<List<NewsDto>> {

        /*
            s.isNullOrBlank() might look weird when coming from Java...
            I mean, if a string "s" is null, wouldn't calling (any) method
            on it lead to a NPE???
            This does not happen based on how kotlin code is compiled (you
            can look into the source code of isNullOrBlank to see how exactly
            this is achieved, eg by inlining and specifying the method can
            be called on nullable objects)
         */

        val list = if (country.isNullOrBlank() && authorId.isNullOrBlank()) {
            crud.findAll()
        } else if (!country.isNullOrBlank() && !authorId.isNullOrBlank()) {
            crud.findAllByCountryAndAuthorId(country, authorId)
        } else if (!country.isNullOrBlank()) {
            crud.findAllByCountry(country)
        } else {
            crud.findAllByAuthorId(authorId!!)
        }
        /*
            Note: here we return a 200 OK even in the case of no data,
            as we still return an [] array (albeit empty).
            Returning a 404 in such a case would be wrong, as the collection
            does exist, even if it is empty.
         */
        return ResponseEntity.ok(NewsConverter.transform(list))
    }


    @ApiOperation("Create a news")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(code = 201, message = "The id of newly created news")
    fun createNews(
            @ApiParam("Text of news, plus author id and country. Should not specify id or creation time")
            @RequestBody
            dto: NewsDto)
            : ResponseEntity<Long> {

        if (!dto.id.isNullOrEmpty()) {
            //Cannot specify id for a newly generated news
            return ResponseEntity.status(400).build()
        }

        if (dto.creationTime != null) {
            //Cannot specify creationTime for a newly generated news
            return ResponseEntity.status(400).build()
        }

        if (dto.authorId == null || dto.text == null || dto.country == null) {
            return ResponseEntity.status(400).build()
        }

        val id: Long?
        try {
            id = crud.createNews(dto.authorId!!, dto.text!!, dto.country!!)
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(201).body(id)
    }


    @ApiOperation("Get a single news specified by id")
    @GetMapping(path = ["/{id}"])
    fun getNews(@ApiParam(ID_PARAM)
                @PathVariable("id")
                pathId: String?)
            : ResponseEntity<NewsDto> {

        val id: Long
        try {
            id = pathId!!.toLong()
        } catch (e: Exception) {
            /*
                invalid id. But here we return 404 instead of 400,
                as in the API we defined the id as string instead of long
             */
            return ResponseEntity.status(404).build()
        }

        val dto = crud.findById(id).orElse(null) ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(NewsConverter.transform(dto))
    }


    @ApiOperation("Update an existing news")
    @PutMapping(path = ["/{id}"], consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun update(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            pathId: String,
            //
            @ApiParam("The news that will replace the old one. Cannot change its id though.")
            @RequestBody
            dto: NewsDto
    ): ResponseEntity<Any> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            /*
                invalid id. But here we return 404 instead of 400,
                as in the API we defined the id as string instead of long
             */
            return ResponseEntity.status(404).build()
        }

        if (dto.id != pathId) {
            // Not allowed to change the id of the resource (because set by the DB).
            // In this case, 409 (Conflict) sounds more appropriate than the generic 400
            return ResponseEntity.status(409).build()
        }

        if (!crud.existsById(id)) {
            //Here, in this API, made the decision to not allow to create a news with PUT.
            // So, if we cannot find it, should return 404 instead of creating it
            return ResponseEntity.status(404).build()
        }

        if (dto.text == null || dto.authorId == null || dto.country == null || dto.creationTime == null) {
            return ResponseEntity.status(400).build()
        }

        try {
            crud.update(id, dto.text!!, dto.authorId!!, dto.country!!, dto.creationTime!!)
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(204).build()
    }


    @ApiOperation("Update the text content of an existing news")
    @PutMapping(path = ["/{id}/text"], consumes = [(MediaType.TEXT_PLAIN_VALUE)])
    fun updateText(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            id: Long?,
            //
            @ApiParam("The new text which will replace the old one")
            @RequestBody
            text: String
    ): ResponseEntity<Any> {
        if (id == null) {
            return ResponseEntity.status(400).build()
        }

        if (!crud.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        try {
            crud.updateText(id, text)
        } catch (e: Exception) {
            if(Throwables.getRootCause(e) is ConstraintViolationException) {
                return ResponseEntity.status(400).build()
            }
            throw e
        }

        return ResponseEntity.status(204).build()
    }


    @ApiOperation("Delete a news with the given id")
    @DeleteMapping(path = ["/{id}"])
    fun delete(@ApiParam(ID_PARAM)
               @PathVariable("id")
               pathId: String?): ResponseEntity<Any> {

        val id: Long
        try {
            id = pathId!!.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (!crud.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        crud.deleteById(id)
        return ResponseEntity.status(204).build()
    }

}

