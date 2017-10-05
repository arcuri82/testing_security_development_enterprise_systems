package org.tsdes.spring.rest.pagination

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.spring.rest.pagination.dto.base.CommentDto
import org.tsdes.spring.rest.pagination.dto.base.NewsDto
import org.tsdes.spring.rest.pagination.dto.base.VoteDto
import org.tsdes.spring.rest.pagination.dto.collection.ListDto
import org.tsdes.spring.rest.pagination.dto.hal.HalLink
import org.tsdes.spring.rest.pagination.entity.News


@Api(value = "/news", description = "Handling of creating and retrieving news")
@RequestMapping(
        path = arrayOf("/news"),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE)
)
@RestController
@Validated
class PaginationRest {

    /*
        As there is only a fixed number of values for "expand",
        I am going to use an enumeration.
     */
    enum class Expand {
        ALL,
        NONE,
        COMMENTS,
        VOTES;

        val isWithComments: Boolean
            get() = this == COMMENTS || this == ALL

        val isWithVotes: Boolean
            get() = this == VOTES || this == ALL
    }


    @Autowired
    private lateinit var service: NewsService


    @ApiOperation("Return a paginated list of current news")
    @GetMapping(produces = arrayOf(Format.HAL_V1))
    fun getNews(
            @ApiParam("The country of the news")
            @RequestParam("country", required = false)
            country: String?,
            @ApiParam("Offset in the list of news")
            @RequestParam("offset", defaultValue = "0")
            offset: Int,
            @ApiParam("Limit of news in a single retrieved page")
            @RequestParam("limit", defaultValue = "10")
            limit: Int,
            @ApiParam("Whether to retrieve or not votes and comments for the given news")
            @RequestParam("expand", defaultValue = "NONE")
            expand: Expand
    ): ResponseEntity<ListDto<NewsDto>> {

        if (offset < 0 || limit < 1) {
            return ResponseEntity.status(400).build()
        }

        /*
            It is generally a really bad idea to read the whole database,
            and even worse make it available from a REST API.
            So, have a max number of entries that will be returned.

            For example, when you search for projects on Github (and they
            do also have REST APIs for it), there is a limit of 1000 results.
         */
        val maxFromDb = 50

        /*
            Based on the "expand" parameter, I will decide whether to load
            or not the votes and comments from the database
         */

        val newsList: List<News>

        newsList = when (expand) {
            Expand.ALL -> service.getNewsList(country, true, true, maxFromDb)
            Expand.NONE -> service.getNewsList(country, false, false, maxFromDb)
            Expand.COMMENTS -> service.getNewsList(country, true, false, maxFromDb)
            Expand.VOTES -> service.getNewsList(country, false, true, maxFromDb)
        }

        /*
            Note: we could cache the results of the JPQL query.
            This is tricky:
            - an internal cache would not work if the load balancer redirect following
              requests to a different running instance
            - caches are inefficient if most of the users will just read the first page
            - if no cache, then there is no guarantee that the data has not been changed
              meanwhile when asking for the "next" page
            - could write the results of JPQL query to disk, but then very inefficient
              if users ask only for one page (plus need to make sure to delete such query
              results after a period of time)

            There is no best solution: all have positive and negative sides.
            When dealing with performance, you need to keep in mind the expected
            usage of the API.
            Here, to make it simple, we simply do not cache the query.
            So, following a "next" link would result in a new database query.
         */

        if (offset != 0 && offset >= newsList.size) {
            return ResponseEntity.status(400).build()
        }

        val dto = DtoTransformer.transform(
                newsList, offset, limit, expand.isWithComments, expand.isWithVotes)

        var builder = UriComponentsBuilder
                .fromPath("/news")
                .queryParam("expand", expand)
                .queryParam("limit", limit)

        if (country != null) {
            builder = builder.queryParam("country", country)
        }

        /*
            Create URL links for "self", "next" and "previous" pages.
            Each page will have up to "limit" NewsDto objects.
            A page is identified by the offset in the list.

            Note: needs to clone the builder, as each call
            like "queryParam" does not create a new one, but
            rather update the existing one
         */

        dto._self = HalLink(builder.cloneBuilder()
                .queryParam("offset", offset)
                .build().toString()
        )

        if (!newsList.isEmpty() && offset > 0) {
            dto.previous = HalLink(builder.cloneBuilder()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            )
        }
        if (offset + limit < newsList.size) {
            dto.next = HalLink(builder.cloneBuilder()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            )
        }

        return ResponseEntity.ok(dto)
    }

    @ApiOperation("Delete the news specified by id")
    @DeleteMapping(path = arrayOf("/{id}"))
    fun deleteNews(@PathVariable("id") newsId: Long): ResponseEntity<Void> {
        service.deleteNews(newsId)
        return ResponseEntity.status(204).build()
    }

    /*
        Note: here I could have "expand" as well as parameter
     */
    @ApiOperation("Retrieved the news specified by id, with all of its comments and votes")
    @GetMapping(path = arrayOf("/{id}"), produces = arrayOf(Format.JSON_V1))
    fun getNews(@PathVariable("id") newsId: Long): ResponseEntity<NewsDto> {

        val news = service.getNews(newsId)
                ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(DtoTransformer.transform(news, true, true))
    }


    @ApiOperation("Create a news with the given text for a given country")
    @PostMapping(consumes = arrayOf(Format.JSON_V1))
    fun createNews(@RequestBody dto: NewsDto): ResponseEntity<Void> {

        val id = service.createNews(dto.text!!, dto.country!!) //FIXME

        return ResponseEntity.created(UriComponentsBuilder
                .fromPath("/news/" + id).build().toUri()
        ).build()
    }

    @ApiOperation("Create a new vote for the given news identified by id")
    @PostMapping(path = arrayOf("/{id}/votes"),
            consumes = arrayOf(Format.JSON_V1))
    fun createVote(
            @PathVariable("id")
            newsId: Long,
            @RequestBody
            voteDto: VoteDto): ResponseEntity<Void> {

        service.createVote(newsId, voteDto.user!!) //FIXME

        return ResponseEntity.status(201).build()
    }

    @ApiOperation("Create a new comment for the given news identified by id")
    @PostMapping(path = arrayOf("/{id}/comments"),
            consumes = arrayOf(Format.JSON_V1))
    fun createComment(
            @PathVariable("id")
            newsId: Long,
            @RequestBody
            commentDto: CommentDto): ResponseEntity<Void> {

        service.createComment(newsId, commentDto.text!!) //FIXME

        return ResponseEntity.status(201).build()
    }

}