package org.tsdes.advanced.exercises.cardgame.usercollections

import org.tsdes.advanced.exercises.cardgame.usercollections.db.CardCopy
import org.tsdes.advanced.exercises.cardgame.usercollections.db.User
import org.tsdes.advanced.exercises.cardgame.usercollections.dto.CardCopyDto
import org.tsdes.advanced.exercises.cardgame.usercollections.dto.UserDto


object DtoConverter {


    fun transform(user: User) : UserDto{

        return UserDto().apply {
            userId = user.userId
            coins = user.coins
            cardPacks = user.cardPacks
            ownedCards = user.ownedCards.map { transform(it) }.toMutableList()
        }
    }

    fun transform(cardCopy: CardCopy) : CardCopyDto{
        return CardCopyDto().apply {
            cardId = cardCopy.cardId
            numberOfCopies = cardCopy.numberOfCopies
        }
    }
}