package org.tsdes.advanced.rest.pagination.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel(description = "A comment on a news")
class CommentDto(

        id: Long? = null,

        @ApiModelProperty("The text of the comment")
        var text: String? = null


) : BaseDto(id)
