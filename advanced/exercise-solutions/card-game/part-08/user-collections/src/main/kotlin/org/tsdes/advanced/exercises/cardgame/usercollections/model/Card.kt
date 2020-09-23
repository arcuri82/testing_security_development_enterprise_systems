package org.tsdes.advanced.exercises.cardgame.usercollections.model

import org.tsdes.advanced.exercises.cardgame.cards.dto.CardDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity


data class Card(
        val cardId : String,
        val rarity: Rarity
){

    constructor(dto: CardDto): this(
            dto.cardId ?: throw IllegalArgumentException("Null cardId"),
            dto.rarity ?: throw IllegalArgumentException("Null rarity"))
}