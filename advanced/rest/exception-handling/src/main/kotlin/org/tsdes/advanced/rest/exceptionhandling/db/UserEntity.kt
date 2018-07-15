package org.tsdes.advanced.rest.exceptionhandling.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Entity
class UserEntity(

        @get:NotBlank
        @get:Size(min=0, max=64)
        var name: String,

        @get:NotNull
        @get:Min(0)
        var age: Int,

        @get:Id @get:GeneratedValue
        var id: Long? = null
)


