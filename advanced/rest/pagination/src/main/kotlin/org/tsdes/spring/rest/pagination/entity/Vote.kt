package org.tsdes.spring.rest.pagination.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
class Vote(


        @get:Id
        @get:GeneratedValue
        var id: Long? = null,

        var user: String? = null,

        @get:ManyToOne
        var news: News
)