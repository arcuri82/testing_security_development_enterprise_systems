package org.tsdes.spring.rest.newsrest.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.tsdes.spring.examples.news.NewsRepository
import org.tsdes.spring.examples.news.constraint.Country
import org.tsdes.spring.rest.newsrest.dto.NewsConverter
import org.tsdes.spring.rest.newsrest.dto.NewsDto
import javax.validation.ConstraintViolationException
import javax.validation.Valid

/**
 * Created by arcuri82 on 13-Jul-17.
 */
@Api(value = "/news", description = "Handling of creating and retrieving news")
@RequestMapping(
        path = arrayOf("/news"), // when the url is "<base>/news", then this class will be used to handle it
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE) // states that, when a method returns something, it is in Json
)
@RestController
@Validated // This is needed to do automated input validation
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

    @ApiOperation("Get all the news")
    @GetMapping
    fun get(): ResponseEntity<List<NewsDto>> {
        return ResponseEntity.ok(NewsConverter.transform(crud.findAll()))
    }

    /*
      NOTE: in the following, we use the URI path to
      identify the subsets that we want, like "country"
      and "author". This does work, but is NOT fully correct.
      Later, we will go back on this point once we discuss
      URI parameters.
   */

    @ApiOperation("Get all the news in the specified country")
    @GetMapping(path = arrayOf("/countries/{country}"))
    fun getByCountry(@ApiParam("The country name")
                     @PathVariable("country")
                     @Valid @Country
                     country: String): ResponseEntity<List<NewsDto>> {
        return ResponseEntity.ok(NewsConverter.transform(crud.findAllByCountry(country)))
    }


    @ApiOperation("Get all the news written by the specified author")
    @GetMapping(path = arrayOf("/authors/{author}"))
    fun getByAuthor(@ApiParam("The id of the author who wrote the news")
                    @PathVariable("author")
                    author: String): ResponseEntity<List<NewsDto>> {
        return ResponseEntity.ok(NewsConverter.transform(crud.findAllByAuthorId(author)))
    }

    @ApiOperation("Get all the news from a given country written by a given author")
    @GetMapping(path = arrayOf("/countries/{country}/authors/{author}"))
    fun getByCountryAndAuthor(
            @ApiParam("The country name")
            @PathVariable("country")
            @Country
            country: String,
            //
            @ApiParam("The id of the author who wrote the news")
            @PathVariable("author")
            author: String)
            : ResponseEntity<List<NewsDto>> {

        return ResponseEntity.ok(NewsConverter.transform(crud.findAllByCountryAndAuthorId(country, author)))
    }

    @ApiOperation("Create a news")
    @PostMapping(consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(code = 201, message = "The id of newly created news")
    fun createNews(
            @ApiParam("Text of news, plus author id and country. Should not specify id or creation time")
            @RequestBody
            dto: NewsDto)
            : ResponseEntity<Long> {

        /*
            Error code 400:
            the user had done something wrong, eg sent invalid input configurations
         */

        if (dto.id != null) {
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
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        if (id == null) {
            //this likely would happen only if bug
            return ResponseEntity.status(500).build()
        }

        return ResponseEntity.status(201).body(id)
    }


    @ApiOperation("Get a single news specified by id")
    @GetMapping(path = arrayOf("/id/{id}"))
    fun getById(@ApiParam(ID_PARAM)
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

        val entity = crud.findOne(id) ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(NewsConverter.transform(entity))
    }


    /*
       PUT is idempotent (ie, applying 1 or 1000 times should end up in same result on the server).
       However, it will replace the whole resource (News) in this case.

       In some cases, a PUT on an non-existing resource might create it.
       This depends on the application.
       Here, as the id is what automatically generate by Hibernate,
       we will not allow it
    */

    @ApiOperation("Update an existing news")
    @PutMapping(path = arrayOf("/id/{id}"), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun update(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            pathId: String?,
            //
            @ApiParam("The news that will replace the old one. Cannot change its id though.")
            @RequestBody
            dto: NewsDto
    ): ResponseEntity<Any> {
        val id: Long
        try {
            id = dto.id!!.toLong()
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

        if (!crud.exists(id)) {
            //Here, in this API, made the decision to not allow to create a news with PUT.
            // So, if we cannot find it, should return 404 instead of creating it
            return ResponseEntity.status(404).build()
        }

        if (dto.text == null || dto.authorId == null || dto.country == null || dto.creationTime == null) {
            return ResponseEntity.status(400).build()
        }

        try {
            crud.update(id, dto.text!!, dto.authorId!!, dto.country!!, dto.creationTime!!)
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(204).build()
    }


    /*
      If we only want to update the text, using the method above can be inefficient, as we
      have to send again the WHOLE news. Partial updates are wrong.
      But, we can have a new resource specifying the content of the news,
      which then would be allowed to update with a PUT.

      Another approach is to use PATCH, but likely on overkill here...
   */

    @ApiOperation("Update the text content of an existing news")
    @PutMapping(path = arrayOf("/id/{id}/text"), consumes = arrayOf(MediaType.TEXT_PLAIN_VALUE))
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

        if (!crud.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        try {
            crud.updateText(id, text)
        } catch (e: ConstraintViolationException) {
            return ResponseEntity.status(400).build()
        }

        return ResponseEntity.status(204).build()
    }


    @ApiOperation("Delete a news with the given id")
    @DeleteMapping(path = arrayOf("/id/{id}"))
    fun delete(@ApiParam(ID_PARAM)
               @PathVariable("id")
               pathId: String?): ResponseEntity<Any> {

        val id: Long
        try {
            id = pathId!!.toLong()
        } catch (e: Exception) {
            /*
                If the above fails, it means the variable was not
                a proper number. So could make sense here to
                return 400 instead of 404, as we know that
                the request is wrong
             */
            return ResponseEntity.status(400).build()
        }

        /*
            Bit tricky: once a resource is deleted, if you try to
            delete it a second time, then you would expect a 404,
            ie resource not found.
            However, DELETE is idempotent, which might be confusing
            in this context. Doing 1 or 100 deletes on the server
            will not alter the result on the server (the resource is
            deleted), although the return values (204 vs 404) can
            be different (this does not affect the definition of
            idempotent)
         */
        if (!crud.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        crud.delete(id)
        return ResponseEntity.status(204).build()
    }


    /*
        This is one case in which JEE is actually better than Spring.
        You might want to have constraints on user inputs directly
        as annotations in method parameters, like it is done for
        example on EJBs.
        Unfortunately, Spring does not do such validation by default.
        See poor excuse/motivation at:

        https://github.com/spring-projects/spring-boot/issues/6228
        https://github.com/spring-projects/spring-boot/issues/6574

        This means we need to manually register an exception handler.
        Every time a ConstraintViolationException is thrown, instead
        of ending up in a 500 error, we catch it are return 400.

        Important: we also need to add @Validated on this class.
     */
    @ExceptionHandler(value = ConstraintViolationException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleValidationFailure(ex: ConstraintViolationException): String {

        val messages = StringBuilder()

        for (violation in ex.constraintViolations) {
            messages.append(violation.message + "\n")
        }

        return messages.toString()
    }
}

const val ID_PARAM = "The numeric id of the news"