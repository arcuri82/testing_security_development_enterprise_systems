package org.tsdes.advanced.frontend.sparest.backend

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.frontend.sparest.backend.db.BookRepository
import org.tsdes.advanced.frontend.sparest.dto.BookDto
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.advanced.rest.exception.RestResponseFactory


@Api(value = "/books", description = "Handling of creating and retrieving book entries")
@RequestMapping(path = ["/books"])
@RestController
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

//    @ApiOperation("Create a new book")
//    @PostMapping
//    fun create(): ResponseEntity<WrappedResponse<Void>> {
//
//
//    }

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
    @GetMapping(path = ["/{id}"])
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
}