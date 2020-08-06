package org.tsdes.advanced.exercises.cardgame.cards

import org.tsdes.advanced.exercises.cardgame.cards.dto.CardDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.CollectionDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.BRONZE
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.SILVER
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.GOLD
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.PINK_DIAMOND


object CardCollection{

    fun get() : CollectionDto{

        val dto = CollectionDto()

        dto.prices.run {
            put(BRONZE, 100)
            put(SILVER, 500)
            put(GOLD,   1_000)
            put(PINK_DIAMOND, 100_000)
        }

        dto.prices.forEach { dto.millValues[it.key] = it.value / 4 }

        dto.rarityProbabilities.run {
            put(SILVER, 0.10)
            put(GOLD, 0.01)
            put(PINK_DIAMOND, 0.001)
            put(BRONZE, 1 - get(SILVER)!! - get(GOLD)!! - get(PINK_DIAMOND)!!)
        }

        return dto
    }

    private fun addCards(dto: CollectionDto){

        val monster = "/1236106-monsters/svg"

        dto.cards.run {
            add(CardDto("c000", "Green Mold", "lore ipsum", BRONZE, "$monster/035-monster.svg"))
            add(CardDto("c001", "Opera Singer", "lore ipsum", BRONZE, "$monster/056-monster.svg"))
            add(CardDto("c002", "Not Stitch", "lore ipsum", BRONZE, "$monster/070-monster.svg"))
            add(CardDto("c003", "Assault Hamster", "lore ipsum", BRONZE, "$monster/100-monster.svg"))
            add(CardDto("c004", "WTF?!?", "lore ipsum", BRONZE, "$monster/075-monster.svg"))
            add(CardDto("c005", "Stupid Lump", "lore ipsum", BRONZE, "$monster/055-monster.svg"))
            add(CardDto("c006", "Sad Farter", "lore ipsum", BRONZE, "$monster/063-monster.svg"))
            add(CardDto("c007", "Smelly Tainter", "lore ipsum", BRONZE, "$monster/050-monster.svg"))
            add(CardDto("c008", "Hårboll", "lore ipsum", BRONZE, "$monster/019-monster.svg"))
            add(CardDto("c009", "Blue Horned", "lore ipsum", BRONZE, "$monster/006-monster.svg"))
            add(CardDto("c010", "Børje McTrumf", "lore ipsum", SILVER, "$monster/081-monster.svg"))
            add(CardDto("c011", "Exa Nopass", "lore ipsum", SILVER, "$monster/057-monster.svg"))
            add(CardDto("c012", "Dick Tracy", "lore ipsum", SILVER, "$monster/028-monster.svg"))
            add(CardDto("c013", "Marius Mario", "lore ipsum", SILVER, "$monster/032-monster.svg"))
            add(CardDto("c014", "Patrick Stew", "lore ipsum", SILVER, "$monster/002-monster.svg"))
            add(CardDto("c015", "Fluffy The Hugger of Death", "lore ipsum", GOLD, "$monster/036-monster.svg"))
            add(CardDto("c016", "Gary The Wise", "lore ipsum", GOLD, "$monster/064-monster.svg"))
            add(CardDto("c017", "Grump-Grump The Grumpy One", "lore ipsum", GOLD, "$monster/044-monster.svg"))
            add(CardDto("c018", "Bert-ho-met The Polite Guy", "lore ipsum", GOLD, "$monster/041-monster.svg"))
            add(CardDto("c019", "Bengt The Destroyer", "lore ipsum", PINK_DIAMOND, "$monster/051-monster.svg"))
        }

        assert(dto.cards.size == dto.cards.map { it.cardId }.toSet().size)
        assert(dto.cards.size == dto.cards.map { it.name }.toSet().size)
        assert(dto.cards.size == dto.cards.map { it.imageId }.toSet().size)
    }

}