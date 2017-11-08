package org.tsdes.spring.security.database.db

import org.hibernate.validator.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@Entity
@Table(name="USERS")
class User (

        @get:Id
        @get:NotBlank
        var username: String?,

        @get:NotBlank
        var password: String?,

        @get:NotNull
        var enabled: Boolean? = true
)