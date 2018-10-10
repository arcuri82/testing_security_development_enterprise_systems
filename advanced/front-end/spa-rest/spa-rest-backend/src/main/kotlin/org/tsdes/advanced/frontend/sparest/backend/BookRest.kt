package org.tsdes.advanced.frontend.sparest.backend

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.frontend.sparest.backend.db.Book
import org.tsdes.advanced.frontend.sparest.backend.db.BookRepository
import org.tsdes.advanced.frontend.sparest.dto.BookDto
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.advanced.rest.exception.RestResponseFactory
import java.net.URI


@Api(value = "/books", description = "Handling of creating and retrieving book entries")
@RequestMapping(path = ["/books"])
@RestController
@Validated
class BookRest(
        val repository: BookRepository
) {


    /*
        Note: here we could have used pagination
     */
    @ApiOperation("Get all the books")
    @GetMapping
    fun getAll(): ResponseEntity<WrappedResponse<List<BookDto>>> {

        return ResponseEntity.status(200).body(
                WrappedResponse(
                        code = 200,
                        data = DtoConverter.transform(repository.findAll()))
                        .validated()
        )
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

        return RestResponseFactory.created(URI.create("/books/" + entity.id))
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
            return RestResponseFactory.userFailure("Invalid id '$pathId'")
        }

        val book = repository.findById(id).orElse(null)
                ?: return RestResponseFactory.notFound(
                        "The requested booked with id '$id' is not in the database")

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
            return RestResponseFactory.userFailure("Invalid id '$pathId'")
        }

        if (!repository.existsById(id)) {
            return RestResponseFactory.notFound(
                    "The requested booked with id '$id' is not in the database")
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
            return RestResponseFactory.userFailure("Invalid id '$pathId'")
        }

        if(dto.id == null){
            return RestResponseFactory.userFailure("Missing id JSON payload")
        }

        if(dto.id != pathId){
            return RestResponseFactory.userFailure("Inconsistent id between URL and JSON payload", 409)
        }

        val entity = repository.findById(id).orElse(null)
            ?: return RestResponseFactory.notFound(
                    "The requested booked with id '$id' is not in the database. " +
                            "This PUT operation will not create it.")

        entity.author = dto.author!!
        entity.title = dto.title!!
        entity.year = dto.year!!

        repository.save(entity)

        return RestResponseFactory.noPayload(204)
    }
}