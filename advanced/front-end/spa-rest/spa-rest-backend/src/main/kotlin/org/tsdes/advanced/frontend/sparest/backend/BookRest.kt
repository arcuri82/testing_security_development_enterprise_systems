package org.tsdes.advanced.frontend.sparest.backend

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.frontend.sparest.backend.db.BookRepository
import org.tsdes.advanced.frontend.sparest.dto.BookDto
import org.tsdes.advanced.rest.dto.WrappedResponse


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
    fun get(): ResponseEntity<WrappedResponse<List<BookDto>>> {

        return ResponseEntity.status(200).body(
                WrappedResponse(
                        code = 200,
                        data = DtoConverter.transform(repository.findAll()))
                        .validated()
        )
    }


    @ApiOperation("Get a specific book, by id")
    @GetMapping(path = ["/{id}"])
    fun getById(
            @ApiParam("The id of the book")
            @PathVariable("id")
            pathId: String
    ): ResponseEntity<WrappedResponse<BookDto>> {

        //FIXME
        var id: Long
//        try {
        id = pathId.toLong()
//        } catch (e: Exception) {
//           //TODO
//            //return
//        }

        val book = repository.findById(id).orElse(null)
                ?: return ResponseEntity.status(404).body(
                        WrappedResponse<BookDto>(code = 404,
                                message = "The requested booked with id '$id' is not in the database")
                                .validated()
                )

        return ResponseEntity.status(200).body(
                WrappedResponse(
                        code = 200,
                        data = DtoConverter.transform(book))
                        .validated()
        )
    }


}