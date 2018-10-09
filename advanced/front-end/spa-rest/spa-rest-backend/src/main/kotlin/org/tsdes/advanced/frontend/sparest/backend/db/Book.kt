package org.tsdes.advanced.frontend.sparest.backend.db

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.validation.constraints.Max

class Book(

        @get:NotBlank @get:Size(max = 32)
        var title: String,

        @get:NotBlank @get:Size(max = 32)
        var author: String,

        @get:Max(2200)
        var year: Int,

        @get:Id @get:GeneratedValue
        var id: Long?
)