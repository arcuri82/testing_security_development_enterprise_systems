package org.tsdes.advanced.security.distributedsession.userservice

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Email

@Entity
class UserInfoEntity(

        @get:Id
        @get:NotBlank
        var userId: String?,

        @get:NotBlank
        var name: String?,

        var middleName: String?,

        @get:NotBlank
        var surname: String?,

        @get:Email
        var email: String?
)