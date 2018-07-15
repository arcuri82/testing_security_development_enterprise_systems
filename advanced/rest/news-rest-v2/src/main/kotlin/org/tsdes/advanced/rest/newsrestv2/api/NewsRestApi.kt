package org.tsdes.advanced.rest.newsrestv2.api

import com.google.common.base.Throwables
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.examplenews.NewsRepository
import org.tsdes.advanced.examplenews.constraint.Country
import org.tsdes.advanced.rest.newsrestv2.dto.NewsConverter
import org.tsdes.advanced.rest.newsrestv2.dto.NewsDto
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Deprecated


const val ID_PARAM = "The numeric id of the news"
const val BASE_JSON = "application/json;charset=UTF-8"

/*
    note the "vnd." (which starts for "vendor") and the
    "+json" (ie, treat it having JSON structure/syntax)
*/
const val V2_NEWS_JSON = "application/vnd.tsdes.news+json;charset=UTF-8;version=2"



/**
 * Created by arcuri82 on 13-Jul-17.
 */
@Api(value = "/news", description = "Handling of creating and retrieving news")
@RequestMapping(
        path = ["/news"], // when the url is "<base>/news", then this class will be used to handle it
        produces = [V2_NEWS_JSON, BASE_JSON]
)
@RestController
class NewsRestApi {


    @Autowired
    private lateinit var crud: NewsRepository

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath : String

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
    @GetMapping
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
            crud.findAllByCountryAndAuthorId(country!!, authorId!!)
        } else if (!country.isNullOrBlank()) {
            crud.findAllByCountry(country!!)
        } else {
            crud.findAllByAuthorId(authorId!!)
        }

        return ResponseEntity.ok(NewsConverter.transform(list))
    }


    @ApiOperation("Create a news")
    @PostMapping(consumes = [V2_NEWS_JSON, BASE_JSON])
    @ApiResponse(code = 201, message = "The id of newly created news")
    fun createNews(
            @ApiParam("Text of news, plus author id and country. Should not specify id or creation time")
            @RequestBody
            dto: NewsDto)
            : ResponseEntity<Long> {

        if (!(dto.id.isNullOrEmpty() && dto.newsId.isNullOrEmpty())) {
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

    /*
        In the following, we changed the URL from "/news/id/{id}"  to "/news/{id}"
     */


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
            pathId: String?,
            //
            @ApiParam("The news that will replace the old one. Cannot change its id though.")
            @RequestBody
            dto: NewsDto
    ): ResponseEntity<Any> {
        val dtoId: Long
        try {
            dtoId = getNewsId(dto)!!.toLong()
        } catch (e: Exception) {
            /*
                invalid id. But here we return 404 instead of 400,
                as in the API we defined the id as string instead of long
             */
            return ResponseEntity.status(404).build()
        }

        if (getNewsId(dto) != pathId) {
            // Not allowed to change the id of the resource (because set by the DB).
            // In this case, 409 (Conflict) sounds more appropriate than the generic 400
            return ResponseEntity.status(409).build()
        }

        if (!crud.existsById(dtoId)) {
            //Here, in this API, made the decision to not allow to create a news with PUT.
            // So, if we cannot find it, should return 404 instead of creating it
            return ResponseEntity.status(404).build()
        }

        if (dto.text == null || dto.authorId == null || dto.country == null || dto.creationTime == null) {
            return ResponseEntity.status(400).build()
        }

        try {
            crud.update(dtoId, dto.text!!, dto.authorId!!, dto.country!!, dto.creationTime!!)
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


    /**
     * Code used to keep backward compatibility
     */
    private fun getNewsId(dto: NewsDto): String? {

        if (dto.newsId != null) {
            return dto.newsId
        } else {
            return dto.id
        }
    }


    // deprecated methods --------------------------------------------------------------

    /*
        The following, are all "deprecated" methods.
        A deprecated method is something that shouldn't be used,
        as it might be removed in a future release of the API.

        A typical version scheme is:

        X.Y.Z

        X = major version
        Y = minor version
        Z = patch

        Increasing Y or Z should NOT break backward compatibility.
        Increasing X "might" break backward compatibility.

        An approach for maintainability is to keep the old API, but
        then do automated permanent redirect (ie 301) to the new API.
        This only works if the new API is semantically equivalent

        WHY are the following methods deprecated?
        Reason is that the HTTP endpoints were not written "properly".
        Consider the example of:

        (1): /news/authors/{author}

        vs

        (2): /news?author={author}

        A URI needs to represent a clear, hierarchical structure
        of the resources. If "/news" represents the set of
        all news, when what would "/news/authors" be?
        The set of all authors that have written a news?
        If that is the case, then the {author} path element
        would apply to the /news/authors set and not the /news one.

        If I want to get all the news for a given author, then I
        am "filtering" a subset of elements from the /news resource.
        In that case, it is better to use ?k=v parameters.

        Same for /news/id/{id}: what resource would "/id/" represent?
        The id of the set "/news"?
      */

    /*
        Kotlin support: Spring is adding first-class support for Kotlin, but
        current version of SpringFox (in 2018) still has some rough-edges...
        In particular, to get the deprecation properly visualized in Swagger-UI
        we need to use

        @java.lang.Deprecated

        instead of

        @kotlin.Deprecated

        Opened issue on SpringFox at:
        https://github.com/springfox/springfox/issues/2551
     */


    @ApiOperation("Get all the news in the specified country")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @GetMapping(path = ["/countries/{country}"])
    @Deprecated
    fun deprecatedGetByCountry(@ApiParam("The country name")
                               @PathVariable("country")
                               @Country
                               country: String): ResponseEntity<List<NewsDto>> {

        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news")
                        .queryParam("country", country)
                        .build().toUri()
                ).build()
    }


    @ApiOperation("Get all the news written by the specified author")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @GetMapping(path = ["/authors/{author}"])
    @Deprecated
    fun deprecatedGetByAuthor(@ApiParam("The id of the author who wrote the news")
                    @PathVariable("author")
                    author: String): ResponseEntity<List<NewsDto>> {

        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news")
                        .queryParam("authorId", author)
                        .build().toUri()
                ).build()
    }

    @ApiOperation("Get all the news from a given country written by a given author")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @GetMapping(path = ["/countries/{country}/authors/{author}"])
    @Deprecated
    fun deprecatedGetByCountryAndAuthor(
            @ApiParam("The country name")
            @PathVariable("country")
            @Country
            country: String,
            //
            @ApiParam("The id of the author who wrote the news")
            @PathVariable("author")
            author: String)
            : ResponseEntity<List<NewsDto>> {


        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news")
                        .queryParam("country", country)
                        .queryParam("authorId", author)
                        .build().toUri()
                ).build()
    }

    @ApiOperation("Get a single news specified by id")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @GetMapping(path = ["/id/{id}"])
    @Deprecated
    fun deprecatedGetById(@ApiParam(ID_PARAM)
                          @PathVariable("id")
                          pathId: String?)
            : ResponseEntity<NewsDto> {

        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news/$pathId").build().toUri()
                ).build()
    }

    @ApiOperation("Update an existing news")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @PutMapping(path = ["/id/{id}"], consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    @Deprecated
    fun deprecatedUpdate(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            pathId: String?,
            //
            @ApiParam("The news that will replace the old one. Cannot change its id though.")
            @RequestBody
            dto: NewsDto
    ): ResponseEntity<Any> {

        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news/$pathId").build().toUri()
                ).build()
    }

    @ApiOperation("Update the text content of an existing news")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @PutMapping(path = ["/id/{id}/text"], consumes = [(MediaType.TEXT_PLAIN_VALUE)])
    @Deprecated
    fun deprecatedUpdateText(
            @ApiParam(ID_PARAM)
            @PathVariable("id")
            id: Long?,
            //
            @ApiParam("The new text which will replace the old one")
            @RequestBody
            text: String
    ): ResponseEntity<Any> {

        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news/$id/text").build().toUri()
                ).build()
    }

    @ApiOperation("Delete a news with the given id")
    @ApiResponses(ApiResponse(code = 301, message = "Deprecated URI. Moved permanently."))
    @DeleteMapping(path = ["/id/{id}"])
    @Deprecated
    fun deprecatedDelete(@ApiParam(ID_PARAM)
                         @PathVariable("id")
                         pathId: String?): ResponseEntity<Any> {

        return ResponseEntity
                .status(301)
                .location(UriComponentsBuilder.fromUriString("$contextPath/news/$pathId").build().toUri()
                ).build()
    }

}

