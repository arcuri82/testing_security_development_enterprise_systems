package org.tsdes.advanced.exercises.cardgame.cards.dto

import io.swagger.annotations.ApiModelProperty


class CollectionDto(

        @get:ApiModelProperty("Info on all the cards in the game")
        var cards : MutableList<CardDto> = mutableListOf(),

        @get:ApiModelProperty("Cost of each card, based on its rarity")
        var prices: MutableMap<Rarity, Int> = mutableMapOf(),

        @get:ApiModelProperty("Milling/sell value of each card, based on its rarity")
        var millValues: MutableMap<Rarity, Int> = mutableMapOf(),

        @get:ApiModelProperty("Probability of getting a card of a specific rarity when opening card packs")
        var rarityProbabilities: MutableMap<Rarity, Double> = mutableMapOf()
)