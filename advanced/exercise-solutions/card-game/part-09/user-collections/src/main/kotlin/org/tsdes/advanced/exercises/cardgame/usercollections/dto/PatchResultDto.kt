package org.tsdes.advanced.exercises.cardgame.usercollections.dto

import io.swagger.annotations.ApiModelProperty


class PatchResultDto(

        @get:ApiModelProperty("If a card pack was opened, specify which cards were in it")
        var cardIdsInOpenedPacket: MutableList<String> = mutableListOf()
)