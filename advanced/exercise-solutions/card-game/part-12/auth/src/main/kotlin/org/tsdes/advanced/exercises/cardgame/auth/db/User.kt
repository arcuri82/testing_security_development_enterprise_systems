package  org.tsdes.advanced.exercises.cardgame.auth.db

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.NotBlank

/**
 * Created by arcuri82 on 08-Nov-17.
 */
@Entity
@Table(name="USERS")
class User(

        @get:Id
        @get:NotBlank
        var username: String?,

        @get:NotBlank
        var password: String?,

        @get:ElementCollection
        @get:CollectionTable(name="authorities", joinColumns = [JoinColumn(name = "username")])
        @get:Column(name="authority")
        @get:NotNull
        var roles: Set<String>? = setOf(),

        @get:NotNull
        var enabled: Boolean? = true
)