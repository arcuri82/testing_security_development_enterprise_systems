package org.tsdes.advanced.exercises.cardgame.scores.dto


data class UserStatsDto(

        var userId: String? = null,

        var victories : Int? = null,

        var defeats: Int? = null,

        var draws : Int? = null,

        var score: Int? = null
)