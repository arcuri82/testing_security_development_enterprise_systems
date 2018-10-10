package org.tsdes.advanced.frontend.sparest.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull


class BookDto(

        @ApiModelProperty("The title of the book")
        @get:NotNull
        var title: String? =null,

        @ApiModelProperty("The author of the book")
        @get:NotNull
        var author: String? = null,

        @ApiModelProperty("The year in which the book was first published")
        @get:NotNull
        var year: Int? = null,

        @ApiModelProperty("The unique id of the book")
        var id: String? = null
)