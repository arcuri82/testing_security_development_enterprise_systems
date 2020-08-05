package org.tsdes.advanced.exercises.cardgame.usercollections.dto


data class UserDto(

    var userId: String? = null,

    var coins: Int? = null,

    var cardPacks: Int? = null,

    var ownedCards : MutableList<CardCopyDto> = mutableListOf()
)