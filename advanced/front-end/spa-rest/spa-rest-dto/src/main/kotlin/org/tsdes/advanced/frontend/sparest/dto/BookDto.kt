package org.tsdes.advanced.frontend.sparest.dto

import io.swagger.annotations.ApiModelProperty


class BookDto(

        @ApiModelProperty("The title of the book")
        var title: String?,

        @ApiModelProperty("The author of the book")
        var author: String?,

        @ApiModelProperty("The year in which the book was first published")
        var year: Int?,

        @ApiModelProperty("The unique id of the book")
        var id: String?
)