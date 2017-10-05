package org.tsdes.spring.rest.pagination.dto.base

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel(description = "A news")
class NewsDto(

        id: Long?,

        @ApiModelProperty("The text of the news")
        var text: String?,

        @ApiModelProperty("The country this news is related to")
        var country: String?,

        @ApiModelProperty("A list of comments made by users on this news")
        var comments: MutableList<CommentDto> = mutableListOf(),

        @ApiModelProperty("A list of votes of users that liked this news")
        var votes: MutableList<VoteDto> = mutableListOf()

) : BaseDto(id)