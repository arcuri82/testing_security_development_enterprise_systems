package org.tsdes.advanced.rest.guiv2

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.tsdes.advanced.rest.guiv2.dto.BookDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.advanced.rest.guiv2.db.Book
import org.tsdes.advanced.rest.guiv2.db.BookRepository

import java.net.URI


@Api(value = "/api/books", description = "Handling of creating and retrieving book entries")
@RequestMapping(path = ["/api/books"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@RestController
class BookRest(
        val repository: BookRepository
) {


    @ApiOperation("Get all the books")
    @GetMapping
    fun getAll(
            @RequestParam("keysetId", required = false)
            keysetId: Long?,
            @RequestParam("keysetYear", required = false)
            keysetYear: Int?
    ): ResponseEntity<WrappedResponse<PageDto<BookDto>>> {

        val page = PageDto<BookDto>()

        val n = 5
        val books = DtoConverter.transform(repository.getNextPage(n, keysetId, keysetYear))
        page.list = books

        if(books.size == n){
            val last = books.last()
            page.next = "/api/books?keysetId=${last.id}&keysetYear=${last.year}"
        }

        return RestResponseFactory.payload(200, page)
    }


    @ApiOperation("Create a new book")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(
            @ApiParam("Data for new book")
            @RequestBody
            dto: BookDto
    ): ResponseEntity<WrappedResponse<Void>> {

        if (dto.id != null) {
            return RestResponseFactory.userFailure("Cannot specify an id when creating a new book")
        }

        val entity = Book(dto.title!!, dto.author!!, dto.year!!)
        repository.save(entity)

        return RestResponseFactory.created(URI.create("/api/books/" + entity.id))
    }

    @ApiOperation("Get a specific book, by id")
    @GetMapping(path = ["/{id}"])
    fun getById(
            @ApiParam("The id of the book")
            @PathVariable("id")
            pathId: String
    ): ResponseEntity<WrappedResponse<BookDto>> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return RestResponseFactory.userFailure("Invalid id")
        }

        val book = repository.findById(id).orElse(null)
                ?: return RestResponseFactory.notFound("Book with id $id does not exist")

        return RestResponseFactory.payload(200, DtoConverter.transform(book))
    }


    @ApiOperation("Delete a specific book, by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The id of the book")
            @PathVariable("id")
            pathId: String
    ): ResponseEntity<WrappedResponse<Void>> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return RestResponseFactory.userFailure("Invalid id")
        }

        if (!repository.existsById(id)) {
            return RestResponseFactory.notFound("Book with id $id does not exist")
        }

        repository.deleteById(id)

        return RestResponseFactory.noPayload(204)
    }


    @ApiOperation("Update a specific book")
    @PutMapping(path = ["/{id}"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun updateById(
            @ApiParam("The id of the book")
            @PathVariable("id")
            pathId: String,
            //
            @ApiParam("New data for updating the book")
            @RequestBody
            dto: BookDto
    ): ResponseEntity<WrappedResponse<Void>> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return RestResponseFactory.userFailure("Invalid id")
        }

        if(dto.id == null){
            return RestResponseFactory.userFailure("Missing id")
        }

        if(dto.id != pathId){
            return RestResponseFactory.userFailure("Inconsistency between id ${dto.id} and $pathId", 409)
        }

        val entity = repository.findById(id).orElse(null)
            ?: return return RestResponseFactory.notFound("Book with id $id does not exist")

        entity.author = dto.author!!
        entity.title = dto.title!!
        entity.year = dto.year!!

        repository.save(entity)

        return RestResponseFactory.noPayload(204)
    }
}