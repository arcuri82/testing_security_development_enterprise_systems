package org.tsdes.advanced.exercises.cardgame.scores

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import org.tsdes.advanced.exercises.cardgame.scores.db.UserStatsService


@Service
class MOMListener(
        private val statsService: UserStatsService
) {

    companion object{
        private val log = LoggerFactory.getLogger(MOMListener::class.java)
    }


    @RabbitListener(queues = ["#{queue.name}"])
    fun receiveFromAMQP(userId: String) {
        val ok = statsService.registerNewUser(userId)
        if(ok){
           log.info("Registered new user via MOM: $userId")
        }
    }
}