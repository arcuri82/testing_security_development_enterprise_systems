package org.tsdes.advanced.rest.conditionalchange

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.jetbrains.annotations.NotNull

/**
 * Created by arcuri82 on 20-Jul-17.
 */
@ApiModel("A numeric counter, with name")
data class CounterDto(

        @ApiModelProperty("The name of the counter")
        @NotNull
        var name: String? = null,

        @ApiModelProperty("The numeric value of the counter")
        @NotNull
        var value: Int? = null

)

