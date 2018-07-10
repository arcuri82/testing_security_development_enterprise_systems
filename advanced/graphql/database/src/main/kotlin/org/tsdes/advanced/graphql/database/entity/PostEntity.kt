package org.tsdes.advanced.graphql.database.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class PostEntity(

        @get:ManyToOne
        @get:NotNull
        var author: AuthorEntity,

        @get:NotBlank
        var text: String,

        @get:OneToMany(cascade = [(CascadeType.ALL)], mappedBy = "parentPost")
        var comments: MutableList<CommentEntity> = mutableListOf(),


        @get:Id @get:GeneratedValue
        var id: Long? = null
)