package org.tsdes.advanced.rest.pagination.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
class Comment(

        @get:Id
        @get:GeneratedValue
        var id: Long? = null,

        var text: String?,

        @get:ManyToOne
        var news: News? = null
)