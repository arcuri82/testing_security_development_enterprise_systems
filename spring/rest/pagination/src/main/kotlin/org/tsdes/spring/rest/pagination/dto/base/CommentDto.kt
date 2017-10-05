package org.tsdes.spring.rest.pagination.dto.base

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel(description = "A comment on a news")
class CommentDto(

        id: Long?,

        @ApiModelProperty("The text of the comment")
        var text: String?


) : BaseDto(id)
