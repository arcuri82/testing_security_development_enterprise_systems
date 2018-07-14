package org.tsdes.spring.rest.patch

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * Created by arcuri82 on 20-Jul-17.
 */
@ApiModel("A numeric counter, with name")
data class CounterDto(

        @ApiModelProperty("The unique id that identifies this counter resource")
        var id: Long? = null,

        @ApiModelProperty("The name of the counter")
        var name: String? = null,

        @ApiModelProperty("The numeric value of the counter")
        var value: Int? = null

)

