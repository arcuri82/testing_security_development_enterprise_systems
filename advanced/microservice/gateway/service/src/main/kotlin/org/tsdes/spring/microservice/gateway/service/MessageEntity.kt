package org.tsdes.spring.microservice.gateway.service

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
class MessageEntity(


        var system: String,

        var message: String,

        @get:Id
        @get:GeneratedValue
        var id: Long? = null

)
