package org.tsdes.advanced.rest.exceptionhandling

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.WrappedResponse
import org.tsdes.advanced.rest.exceptionhandling.constraint.NotZero
import org.tsdes.advanced.rest.exceptionhandling.db.UserEntity
import org.tsdes.advanced.rest.exceptionhandling.db.UserRepository
import org.tsdes.advanced.rest.exceptionhandling.dto.DivisionDto
import org.tsdes.advanced.rest.exceptionhandling.exception.RestResponseEntityExceptionHandler
import org.tsdes.advanced.rest.exceptionhandling.exception.UserInputValidationException


@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
//Important, otherwise bean validation would not take place
@Validated
class ExceptionHandlingApi(private val repository: UserRepository) {


    private fun validResponse(x: Int, y: Int): ResponseEntity<WrappedResponse<DivisionDto>> {

        assert(y != 0)

        val result = x / y

        return ResponseEntity.status(200).body(
                WrappedResponse(
                        code = 200,
                        data = DivisionDto(x, y, result))
                        .validated()
        )
    }


    @ApiOperation("Divide x by y")
    @GetMapping(path = ["/divide_v0"])
    fun divideV0(
            @ApiParam("The numerator in the division")
            @RequestParam("x", required = true)
            x: Int,

            @ApiParam("The denominator in the division. Must be non-zero.")
            @RequestParam("y", required = true)
            y: Int

    ): ResponseEntity<WrappedResponse<DivisionDto>> {

        if (y == 0) {
            return ResponseEntity.status(400).body(
                    WrappedResponse<DivisionDto>(
                            code = 400,
                            message = "Cannot divide by 0")
                            .validated()
            )
        }

        return validResponse(x, y)
    }


    @ApiOperation("Divide x by y")
    @GetMapping(path = ["/divide_v1"])
    fun divideV1(
            @ApiParam("The numerator in the division")
            @RequestParam("x", required = true)
            x: Int,

            @ApiParam("The denominator in the division. Must be non-zero.")
            @RequestParam("y", required = true)
            y: Int

    ): ResponseEntity<WrappedResponse<DivisionDto>> {

        if (y == 0) {
            throw UserInputValidationException("Cannot divide by 0")
        }

        return validResponse(x, y)
    }


    @ApiOperation("Simulate a server bug")
    @GetMapping(path = ["/server_bug"])
    fun serverBug(): ResponseEntity<WrappedResponse<Any>> {

        throw RuntimeException("A simulated bug")
    }


    @ApiOperation("Divide x by y")
    @GetMapping(path = ["/divide_v2"])
    fun divideV2(
            @ApiParam("The numerator in the division")
            @RequestParam("x", required = true)
            x: Int,

            @ApiParam("The denominator in the division. Must be non-zero.")
            @RequestParam("y", required = true)
            // needs @Validated on the class to work
            @NotZero
            y: Int

    ): ResponseEntity<WrappedResponse<DivisionDto>> {

        return validResponse(x, y)
    }


    @ApiOperation("Create a new user")
    @PostMapping(path = ["/users"])
    fun createUser(
            @ApiParam("The name of the user")
            @RequestParam("name")
            name: String,

            @ApiParam("The age of the user")
            @RequestParam("age")
            age: Int
    ): ResponseEntity<WrappedResponse<String>> {

        val entity = UserEntity(name, age)

        try {
            repository.save(entity)
        } catch (e: Exception) {
            RestResponseEntityExceptionHandler.handlePossibleConstraintViolation(e)
        }

        return ResponseEntity.status(201).body(
                WrappedResponse(
                        code = 201,
                        data = entity.id!!.toString())
                        .validated()
        )
    }

}