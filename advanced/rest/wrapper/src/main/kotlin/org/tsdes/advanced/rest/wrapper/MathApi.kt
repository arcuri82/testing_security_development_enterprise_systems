package org.tsdes.advanced.rest.wrapper

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.rest.dto.WrappedResponse

@RestController
@RequestMapping(path = ["/math"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class MathApi{

    @ApiOperation("Divide x by y")
    @GetMapping(path = ["/divide"])
    fun divide(
            @ApiParam("The numerator in the division")
            @RequestParam("x", required = true)
            x: Int,

            @ApiParam("The denominator in the division. Must be non-zero.")
            @RequestParam("y", required = true)
            y: Int

    ) : ResponseEntity<WrappedResponse<DivisionDto>> {

        if(y == 0){
            return ResponseEntity.status(400).body(
                    ResponseDto(
                            code = 400,
                            message = "Cannot divide by 0")
                            .validated()
            )
        }

        val result = x / y

        return ResponseEntity.status(200).body(
                ResponseDto(
                        code = 200,
                        data = DivisionDto(x,y,result))
                        .validated()
        )
    }
}