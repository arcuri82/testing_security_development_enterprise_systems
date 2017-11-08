package org.tsdes.spring.security.database.db

import org.jetbrains.annotations.NotNull
import javax.persistence.*

/**
 * Created by arcuri82 on 08-Nov-17.
 */

//see https://docs.spring.io/spring-security/site/docs/4.2.3.RELEASE/reference/htmlsingle/#appendix-schema

@Entity
@Table(name="AUTHORITIES",
        indexes = arrayOf(Index(name = "ix_auth_username ", columnList = "username,authority", unique = true)))
class Authority(

        @get:ManyToOne
    @get:NotNull
    @get:JoinColumn(name ="username")
    var user: User?,

        @get:NotNull
    var authority: String?,

        @get:Id
    @get:GeneratedValue
    var id: Long? = null
)