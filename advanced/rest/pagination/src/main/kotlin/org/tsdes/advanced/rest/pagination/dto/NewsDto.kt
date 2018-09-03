package org.tsdes.advanced.rest.pagination.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel(description = "A news")
class NewsDto(

        id: Long? = null,

        @ApiModelProperty("The text of the news")
        var text: String?  = null,

        @ApiModelProperty("The country this news is related to")
        var country: String? = null,

        @ApiModelProperty("A list of comments made by users on this news")
        var comments: MutableList<CommentDto> = mutableListOf(),

        @ApiModelProperty("A list of votes of users that liked this news")
        var votes: MutableList<VoteDto> = mutableListOf()

) : BaseDto(id)