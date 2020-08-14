package org.tsdes.advanced.exercises.cardgame.usercollections.db

import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Entity
@Table(name="user_data")
class User(

    @get:Id
    @get:NotBlank
    var userId: String? = null,

    @get:Min(0)
    var coins: Int = 0,

    @get:Min(0)
    var cardPacks: Int = 0,

    @get:OneToMany(mappedBy = "user", cascade = [(CascadeType.ALL)])
    var ownedCards : MutableList<CardCopy> = mutableListOf()
)