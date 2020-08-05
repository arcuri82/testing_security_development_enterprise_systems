package org.tsdes.advanced.exercises.cardgame.usercollections.dto

enum class Command{

    OPEN_PACK,

    MILL_CARD,

    BUY_CARD
}

data class PatchUserDto (

    var command : Command? = null,

    var cardId: String? = null
)