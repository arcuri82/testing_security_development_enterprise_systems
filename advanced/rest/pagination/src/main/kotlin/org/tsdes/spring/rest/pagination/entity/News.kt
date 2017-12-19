package org.tsdes.spring.rest.pagination.entity

import javax.persistence.*

@Entity
class News(

        @get:Id
        @get:GeneratedValue
        var id: Long? = null,

        var text: String,

        var country: String,

        /*
            Here I do care about performance, as I will have options in the
            REST endpoint to get News with/without votes and comments.
            If I do need comments, I shouldn't read them from the database
            in the first place.
            This means having a LAZY fetch (which is the default).
            Recall: in a bidirectional relationship, you have to use
            "mappedBy" in the @OneToMany annotation.
         */


        @get:OneToMany(
                fetch = FetchType.LAZY,
                mappedBy = "news",
                cascade = arrayOf(CascadeType.ALL)
        )
        var comments: MutableList<Comment> = mutableListOf(),


        @get:OneToMany(
                fetch = FetchType.LAZY,
                mappedBy = "news",
                cascade = arrayOf(CascadeType.ALL)
        )
        var votes: MutableList<Vote> = mutableListOf()
)