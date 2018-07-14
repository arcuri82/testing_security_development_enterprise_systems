package org.tsdes.advanced.rest.wrapper

import io.swagger.annotations.ApiModelProperty


class DivisionDto(

        @ApiModelProperty("The numerator in the division")
        var x: Int? = null,

        @ApiModelProperty("The denominator in the division. Must be non-zero.")
        var y: Int? = null,

        @ApiModelProperty("The result of dividing x by y")
        var result: Int? = null
)