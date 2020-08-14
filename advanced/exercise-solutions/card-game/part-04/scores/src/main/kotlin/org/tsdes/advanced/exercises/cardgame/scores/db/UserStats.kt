package org.tsdes.advanced.exercises.cardgame.scores.db

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Entity
class UserStats(

        @get:Id @get:NotBlank
        var userId: String? = null,

        @get:Min(0) @get:NotNull
        var victories : Int = 0,

        @get:Min(0) @get:NotNull
        var defeats: Int = 0,

        @get:Min(0) @get:NotNull
        var draws : Int = 0,

        @get:Min(0) @get:NotNull
        var score: Int = 0
)