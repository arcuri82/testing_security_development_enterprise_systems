package org.tsdes.advanced.exercises.cardgame.usercollections

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.tsdes.advanced.exercises.cardgame.cards.dto.CollectionDto
import org.tsdes.advanced.exercises.cardgame.cards.dto.Rarity
import org.tsdes.advanced.exercises.cardgame.usercollections.model.Card
import org.tsdes.advanced.exercises.cardgame.usercollections.model.Collection
import org.tsdes.advanced.rest.dto.WrappedResponse
import javax.annotation.PostConstruct
import kotlin.random.Random


@Service
class CardService(
        private val client: RestTemplate
) {

    companion object{
        private val log = LoggerFactory.getLogger(CardService::class.java)
    }

    @Value("\${cardServiceAddress}")
    private lateinit var cardServiceAddress: String

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

        val version = "v1_000"
        val uri = UriComponentsBuilder
                .fromUriString("http://${cardServiceAddress.trim()}/api/cards/collection_$version")
                .build().toUri()

        val response = try {
            client.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    object : ParameterizedTypeReference<WrappedResponse<CollectionDto>>() {})
        } catch (e: Exception) {
            log.error("Failed to fetch data from Card Service: ${e.message}")
            return
        }

        if(response.statusCodeValue != 200){
            log.error("Error in fetching data from Card Service. Status ${response.statusCodeValue}." +
                    "Message: " + response.body.message)
        }

        try{
            collection = Collection(response.body.data!!)
        } catch(e: Exception){
            log.error("Failed to parse card collection info: ${e.message}")
        }
    }

    private fun verifyCollection(){

        if(collection == null){
            fetchData()

            if(collection == null){
                throw IllegalStateException("No collection info")
            }
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