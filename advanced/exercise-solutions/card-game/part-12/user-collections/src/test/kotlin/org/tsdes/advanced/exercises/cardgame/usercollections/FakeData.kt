package org.tsdes.advanced.exercises.cardgame.usercollections

import org.tsdes.advanced.exercises.cardgame.cards.dto.CardDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.CollectionDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.BRONZE
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.SILVER
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.GOLD
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.PINK_DIAMOND


object FakeData {

    fun getCollectionDto() : CollectionDto{

        val dto = CollectionDto()

        dto.prices[BRONZE] = 100
        dto.prices[SILVER] = 500
        dto.prices[GOLD] = 1_000
        dto.prices[PINK_DIAMOND] = 100_000

        dto.prices.forEach { dto.millValues[it.key] = it.value / 4 }
        dto.prices.keys.forEach { dto.rarityProbabilities[it] = 0.25 }

        dto.cards.run {
            add(CardDto(cardId = "c00", rarity = BRONZE))
            add(CardDto(cardId = "c01", rarity = BRONZE))
            add(CardDto(cardId = "c02", rarity = BRONZE))
            add(CardDto(cardId = "c03", rarity = BRONZE))
            add(CardDto(cardId = "c04", rarity = SILVER))
            add(CardDto(cardId = "c05", rarity = SILVER))
            add(CardDto(cardId = "c06", rarity = SILVER))
            add(CardDto(cardId = "c07", rarity = GOLD))
            add(CardDto(cardId = "c08", rarity = GOLD))
            add(CardDto(cardId = "c09", rarity = PINK_DIAMOND))
        }

        return dto
    }
}