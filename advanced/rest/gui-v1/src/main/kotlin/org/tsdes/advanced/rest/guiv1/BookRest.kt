package org.tsdes.advanced.rest.guiv1

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.tsdes.advanced.rest.guiv1.dto.BookDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.guiv1.db.Book
import org.tsdes.advanced.rest.guiv1.db.BookRepository

import java.net.URI


@Api(value = "/api/books", description = "Handling of creating and retrieving book entries")
@RequestMapping(path = ["/api/books"])
@RestController
class BookRest(
        val repository: BookRepository
) {


    @ApiOperation("Get all the books")
    @GetMapping
    fun getAll(): ResponseEntity<List<BookDto>> {

        return ResponseEntity.status(200).body(
                DtoConverter.transform(repository.findAll())
        )

    }

    @ApiOperation("Create a new book")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(
            @ApiParam("Data for new book")
            @RequestBody
            dto: BookDto
    ): ResponseEntity<Void> {

        if (dto.id != null) {
            // Cannot specify an id when creating a new book
            return ResponseEntity.status(400).build()
        }

        val entity = Book(dto.title!!, dto.author!!, dto.year!!)
        repository.save(entity)

        return ResponseEntity.created(URI.create("/api/books/" + entity.id)).build()
    }

    @ApiOperation("Get a specific book, by id")
    @GetMapping(path = ["/{id}"])
    fun getById(
            @ApiParam("The id of the book")
            @PathVariable("id")
            pathId: String
    ): ResponseEntity<BookDto> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        val book = repository.findById(id).orElse(null)
                ?: return ResponseEntity.status(404).build()

        return ResponseEntity.status(200).body(DtoConverter.transform(book))
    }


    @ApiOperation("Delete a specific book, by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The id of the book")
            @PathVariable("id")
            pathId: String
    ): ResponseEntity<Void> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if (!repository.existsById(id)) {
            return ResponseEntity.status(404).build()
        }

        repository.deleteById(id)

        return return ResponseEntity.status(204).build()
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
    ): ResponseEntity<Void> {

        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return ResponseEntity.status(400).build()
        }

        if(dto.id == null){
            return ResponseEntity.status(400).build()
        }

        if(dto.id != pathId){
            return ResponseEntity.status(409).build()
        }

        val entity = repository.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).build()

        entity.author = dto.author!!
        entity.title = dto.title!!
        entity.year = dto.year!!

        repository.save(entity)

        return ResponseEntity.status(204).build()
    }
}