package org.tsdes.advanced.rest.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.jetbrains.annotations.NotNull

/**
 * Created by arcuri82 on 09-Aug-19.
 */
@ApiModel(description = "Paginated list of resources")
class PageDto<T>(

        @ApiModelProperty("The data contained in the page")
        @get:NotNull
        var list: List<T> = listOf(),

        @ApiModelProperty("Link to the next page, if it exists")
        var next: String? = null
)