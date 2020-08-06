package org.tsdes.advanced.rest.pagination

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.advanced.rest.pagination.dto.hal.PageDto
import org.tsdes.advanced.rest.pagination.dto.CommentDto
import org.tsdes.advanced.rest.pagination.dto.NewsDto
import org.tsdes.advanced.rest.pagination.dto.VoteDto
import org.tsdes.advanced.rest.pagination.entity.News


@Api(value = "/news", description = "Handling of creating and retrieving news")
@RequestMapping(
        path = ["/news"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
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
            get() = (this == COMMENTS || this == ALL)

        val isWithVotes: Boolean
            get() = (this == VOTES || this == ALL)
    }


    @Autowired
    private lateinit var service: NewsService


    @ApiOperation("Return a paginated list of current news")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
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

    /*
        Note: here for simplicity I am not using a WrappedResponse, as to avoid conflating
        together those 2 different concepts in this example
     */
    ): ResponseEntity<PageDto<NewsDto>> {



        /*
            It is generally a really bad idea to read the whole database,
            and even worse make it available from a REST API.
            So, have a max number of entries that will be returned, ie a page.
         */
        val maxPageLimit = 50

        /*
            Even if we have a page limit, the OFFSET-LIMIT approach for pagination
            can be extremely expensive for large Limit values, ie late pages.
            So we force here the reading of a max number of first values from the DB.
         */
        val maxFromDb = 1000

        val onDb = service.numberOfNews(country)

        if (offset < 0 || limit < 1 || limit > maxPageLimit || (offset+limit) > maxFromDb || offset > onDb) {
            return ResponseEntity.status(400).build()
        }
        /*
            Based on the "expand" parameter, I will decide whether to load
            or not the votes and comments from the database
         */

        val newsList: List<News> = when (expand) {
            Expand.ALL -> service.getNewsList(country, true, true, offset, limit)
            Expand.NONE -> service.getNewsList(country, false, false, offset, limit)
            Expand.COMMENTS -> service.getNewsList(country, true, false, offset, limit)
            Expand.VOTES -> service.getNewsList(country, false, true, offset, limit)
        }

        var builder = UriComponentsBuilder
                .fromPath("/news")
                .queryParam("expand", expand)

        if (country != null) {
            builder = builder.queryParam("country", country)
        }

        val dto = DtoTransformer.transform(
                newsList, offset, limit, onDb, maxFromDb, expand.isWithComments, expand.isWithVotes, builder)

        return ResponseEntity.ok(dto)
    }

    @ApiOperation("Delete the news specified by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteNews(@PathVariable("id") newsId: Long): ResponseEntity<Void> {
        service.deleteNews(newsId)
        return ResponseEntity.status(204).build()
    }

    /*
        Note: here I could have "expand" as well as parameter
     */
    @ApiOperation("Retrieved the news specified by id, with all of its comments and votes")
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getNews(@PathVariable("id") newsId: Long): ResponseEntity<NewsDto> {

        val news = service.getNews(newsId)
                ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(DtoTransformer.transform(news, true, true))
    }


    @ApiOperation("Create a news with the given text for a given country")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun createNews(@RequestBody dto: NewsDto): ResponseEntity<Void> {

        if(dto.text == null || dto.country == null){
            return ResponseEntity.status(400).build()
        }

        val id = service.createNews(dto.text!!, dto.country!!)

        return ResponseEntity.created(UriComponentsBuilder
                .fromPath("/news/$id").build().toUri()
        ).build()
    }


    @ApiOperation("Create a new vote for the given news identified by id")
    @PostMapping(path = ["/{id}/votes"],
            consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun createVote(
            @PathVariable("id")
            newsId: Long,
            @RequestBody
            voteDto: VoteDto): ResponseEntity<Void> {

        if(voteDto.user == null){
            return ResponseEntity.status(400).build()
        }

        service.createVote(newsId, voteDto.user!!)

        return ResponseEntity.status(201).build()
    }


    @ApiOperation("Create a new comment for the given news identified by id")
    @PostMapping(path = ["/{id}/comments"],
            consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun createComment(
            @PathVariable("id")
            newsId: Long,
            @RequestBody
            commentDto: CommentDto): ResponseEntity<Void> {

        if(commentDto.text == null){
            return ResponseEntity.status(400).build()
        }

        service.createComment(newsId, commentDto.text!!)

        return ResponseEntity.status(201).build()
    }

}