package org.tsdes.advanced.exercises.cardgame.usercollections.db

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.tsdes.advanced.exercises.cardgame.usercollections.CardService
import javax.persistence.LockModeType


@Repository
interface UserRepository : CrudRepository<User, String>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.userId = :id")
    fun lockedFind(@Param("id") userId: String) : User?

}


@Service
@Transactional
class UserService(
        val userRepository: UserRepository,
        val cardService: CardService
) {

    companion object{
        const val CARDS_PER_PACK = 5
    }

    fun findByIdEager(userId: String) : User?{

        val user = userRepository.findById(userId).orElse(null)
        if(user != null){
            user.ownedCards.size
        }
        return user
    }

    fun registerNewUser(userId: String) : Boolean{

        if(userRepository.existsById(userId)){
            return false
        }

        val user = User()
        user.userId = userId
        user.cardPacks = 3
        user.coins = 100
        userRepository.save(user)
        return true
    }

    private fun validateCard(cardId: String) {
        if (!cardService.isInitialized()) {
            throw IllegalStateException("Card service is not initialized")
        }

        if (!cardService.cardCollection.any { it.cardId == cardId }) {
            throw IllegalArgumentException("Invalid cardId: $cardId")
        }
    }

    private fun validateUser(userId: String) {
        if (!userRepository.existsById(userId)) {
            throw IllegalArgumentException("User $userId does not exist")
        }
    }

    private fun validate(userId: String, cardId: String) {
        validateUser(userId)
        validateCard(cardId)
    }

    fun buyCard(userId: String, cardId: String) {
        validate(userId, cardId)

        val price = cardService.price(cardId)
        val user = userRepository.lockedFind(userId)!!

        if (user.coins < price) {
            throw IllegalArgumentException("Not enough coins")
        }

        user.coins -= price

        addCard(user, cardId)
    }

    private fun addCard(user: User, cardId: String) {
        user.ownedCards.find { it.cardId == cardId }
                ?.apply { numberOfCopies++ }
                ?: CardCopy().apply {
                    this.cardId = cardId
                    this.user = user
                    this.numberOfCopies = 1
                }.also { user.ownedCards.add(it) }
    }

    fun millCard(userId: String, cardId: String) {
        validate(userId, cardId)

        val user = userRepository.lockedFind(userId)!!

        val copy = user.ownedCards.find { it.cardId == cardId }
        if(copy == null || copy.numberOfCopies == 0){
            throw IllegalArgumentException("User $userId does not own a copy of $cardId")
        }

        copy.numberOfCopies--

        val millValue = cardService.millValue(cardId)
        user.coins += millValue
    }

    fun openPack(userId: String) {

        validateUser(userId)

        val user = userRepository.lockedFind(userId)!!

        if(user.cardPacks < 1){
            throw IllegalArgumentException("No pack to open")
        }

        user.cardPacks--

        val selection = cardService.getRandomSelection(CARDS_PER_PACK)

        selection.forEach {
            addCard(user, it.cardId)
        }
    }
}