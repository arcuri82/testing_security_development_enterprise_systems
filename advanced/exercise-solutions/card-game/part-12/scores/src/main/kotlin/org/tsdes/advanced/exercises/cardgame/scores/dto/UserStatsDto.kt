package org.tsdes.advanced.exercises.cardgame.scores.dto

import io.swagger.annotations.ApiModelProperty


data class UserStatsDto(

        @get:ApiModelProperty("The id of the player")
        var userId: String? = null,

        @get:ApiModelProperty("How many victories the player had so far")
        var victories : Int? = null,

        @get:ApiModelProperty("How many defeats the player had so far")
        var defeats: Int? = null,

        @get:ApiModelProperty("How many draws the player had so far")
        var draws : Int? = null,

        @get:ApiModelProperty("The current score of the player")
        var score: Int? = null
)