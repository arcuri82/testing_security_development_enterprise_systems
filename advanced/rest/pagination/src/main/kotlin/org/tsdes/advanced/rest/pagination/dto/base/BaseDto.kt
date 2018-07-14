package org.tsdes.advanced.rest.pagination.dto.base

import io.swagger.annotations.ApiModelProperty


open abstract class BaseDto(

        @ApiModelProperty("The id of this resource")
        var id: Long? = null
)
