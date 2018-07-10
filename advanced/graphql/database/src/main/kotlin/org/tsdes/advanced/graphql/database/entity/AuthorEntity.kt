package org.tsdes.advanced.graphql.database.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class AuthorEntity (

        @get:NotBlank
        var name: String,

        @get:NotBlank
        var surname: String,

        @get:Id @get:GeneratedValue
        var id: Long? = null
)