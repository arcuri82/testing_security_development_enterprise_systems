package org.tsdes.advanced.frontend.sparest.backend.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull

@Entity
class Book(

        @get:NotBlank @get:Size(max = 256)
        var title: String,

        @get:NotBlank @get:Size(max = 64)
        var author: String,

        @get:Max(2200) @get:NotNull
        var year: Int,

        @get:Id @get:GeneratedValue
        var id: Long? = null
)