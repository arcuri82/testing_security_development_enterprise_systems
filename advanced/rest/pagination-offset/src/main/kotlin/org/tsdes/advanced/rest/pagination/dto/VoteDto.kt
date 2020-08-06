package org.tsdes.advanced.rest.pagination.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "A positive vote for a given news, representing a user that liked it")
class VoteDto(

        id: String? = null,

        @ApiModelProperty("The id of the user")
        var user: String? = null


) : BaseDto(id)