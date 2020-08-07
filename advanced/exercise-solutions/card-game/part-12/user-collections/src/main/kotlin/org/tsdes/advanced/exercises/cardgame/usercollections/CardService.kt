package org.tsdes.advanced.exercises.cardgame.usercollections

import org.springframework.stereotype.Service
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity
import org.tsdes.advanced.exercises.cardgame.usercollections.model.Card
import org.tsdes.advanced.exercises.cardgame.usercollections.model.Collection
import javax.annotation.PostConstruct
import kotlin.random.Random


@Service
class CardService {

    protected var collection: Collection? = null

    val cardCollection : List<Card>
        get() = collection?.cards ?: listOf()

    private val lock = Any()

    @PostConstruct
    fun init(){

        synchronized(lock){
            if(cardCollection.isNotEmpty()){
                return
            }
            fetchData()
        }
    }

    fun isInitialized() = cardCollection.isNotEmpty()

    protected fun fetchData(){

        //TODO
    }

    private fun verifyCollection(){
        if(collection == null){
            throw IllegalStateException("No collection info")
        }
    }

    fun millValue(cardId: String) : Int {
        verifyCollection()
        val card : Card = cardCollection.find { it.cardId  == cardId} ?:
            throw IllegalArgumentException("Invalid cardId $cardId")

        return collection!!.millValues[card.rarity]!!
    }

    fun price(cardId: String) : Int {
        verifyCollection()
        val card : Card = cardCollection.find { it.cardId  == cardId} ?:
                throw IllegalArgumentException("Invalid cardId $cardId")

        return collection!!.prices[card.rarity]!!
    }

    fun getRandomSelection(n: Int) : List<Card>{

        if(n <= 0){
            throw IllegalArgumentException("Non-positive n: $n")
        }

        verifyCollection()

        val selection = mutableListOf<Card>()

        val probabilities = collection!!.rarityProbabilities
        val bronze = probabilities[Rarity.BRONZE]!!
        val silver = probabilities[Rarity.SILVER]!!
        val gold = probabilities[Rarity.GOLD]!!
        //val pink = probabilities[Rarity.PINK_DIAMOND]!!

        repeat(n) {
            val p = Math.random()
            val r = when{
                p <= bronze -> Rarity.BRONZE
                p > bronze && p <= bronze + silver -> Rarity.SILVER
                p > bronze + silver && p <= bronze + silver + gold -> Rarity.GOLD
                p > bronze + silver + gold -> Rarity.PINK_DIAMOND
                else -> throw IllegalStateException("BUG for p=$p")
            }
            val card = collection!!.cardsByRarity[r].let{ it!![Random.nextInt(it.size)] }
            selection.add(card)
        }

        return selection
    }
}