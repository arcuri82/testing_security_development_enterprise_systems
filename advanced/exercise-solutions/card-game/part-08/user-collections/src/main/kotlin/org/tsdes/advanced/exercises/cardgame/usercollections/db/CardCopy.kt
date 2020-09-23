package org.tsdes.advanced.exercises.cardgame.usercollections.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Entity
class CardCopy(

    @get:Id @get:GeneratedValue
    var id : Long? = null,

    @get:ManyToOne
    @get:NotNull
    var user : User? = null,

    @get:NotBlank
    var cardId: String? = null,

    @get:Min(0)
    var numberOfCopies : Int = 0
)