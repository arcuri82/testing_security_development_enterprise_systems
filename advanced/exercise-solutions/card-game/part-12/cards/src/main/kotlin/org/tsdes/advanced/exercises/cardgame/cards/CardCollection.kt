package org.tsdes.advanced.exercises.cardgame.cards

import org.tsdes.advanced.exercises.cardgame.cards.dto.CardDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.CollectionDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity.*


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

        addCards(dto)

        return dto
    }

    private fun addCards(dto: CollectionDto){

        dto.cards.run {
            add(CardDto("c000", "Green Mold", "lore ipsum", BRONZE, "035-monster.svg"))
            add(CardDto("c001", "Opera Singer", "lore ipsum", BRONZE, "056-monster.svg"))
            add(CardDto("c002", "Not Stitch", "lore ipsum", BRONZE, "070-monster.svg"))
            add(CardDto("c003", "Assault Hamster", "lore ipsum", BRONZE, "100-monster.svg"))
            add(CardDto("c004", "WTF?!?", "lore ipsum", BRONZE, "075-monster.svg"))
            add(CardDto("c005", "Stupid Lump", "lore ipsum", BRONZE, "055-monster.svg"))
            add(CardDto("c006", "Sad Farter", "lore ipsum", BRONZE, "063-monster.svg"))
            add(CardDto("c007", "Smelly Tainter", "lore ipsum", BRONZE, "050-monster.svg"))
            add(CardDto("c008", "Hårboll", "lore ipsum", BRONZE, "019-monster.svg"))
            add(CardDto("c009", "Blue Horned", "lore ipsum", BRONZE, "006-monster.svg"))
            add(CardDto("c010", "Børje McTrumf", "lore ipsum", SILVER, "081-monster.svg"))
            add(CardDto("c011", "Exa Nopass", "lore ipsum", SILVER, "057-monster.svg"))
            add(CardDto("c012", "Dick Tracy", "lore ipsum", SILVER, "028-monster.svg"))
            add(CardDto("c013", "Marius Mario", "lore ipsum", SILVER, "032-monster.svg"))
            add(CardDto("c014", "Patrick Stew", "lore ipsum", SILVER, "002-monster.svg"))
            add(CardDto("c015", "Fluffy The Hugger of Death", "lore ipsum", GOLD, "036-monster.svg"))
            add(CardDto("c016", "Gary The Wise", "lore ipsum", GOLD, "064-monster.svg"))
            add(CardDto("c017", "Grump-Grump The Grumpy One", "lore ipsum", GOLD, "044-monster.svg"))
            add(CardDto("c018", "Bert-ho-met The Polite Guy", "lore ipsum", GOLD, "041-monster.svg"))
            add(CardDto("c019", "Bengt The Destroyer", "lore ipsum", PINK_DIAMOND, "051-monster.svg"))
        }

        assert(dto.cards.size == dto.cards.map { it.cardId }.toSet().size)
        assert(dto.cards.size == dto.cards.map { it.name }.toSet().size)
        assert(dto.cards.size == dto.cards.map { it.imageId }.toSet().size)
    }

}