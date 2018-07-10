package org.tsdes.advanced.graphql.database.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class CommentEntity(

        @get:ManyToOne
        @get:NotNull
        var author: AuthorEntity,


        @get:ManyToOne
        @get:NotNull
        var parentPost: PostEntity,

        @get:NotBlank
        var text: String,

        @get:Id @get:GeneratedValue
        var id: Long? = null
)