package org.tsdes.advanced.security.distributedsession.userservice

import org.tsdes.advanced.security.distributedsession.userservice.dto.UserInfoDto


object DtoTransformer {


    fun transform(entity: UserInfoEntity) : UserInfoDto{

        return UserInfoDto(
                userId = entity.userId,
                name = entity.name,
                middleName = entity.middleName,
                surname = entity.surname,
                email = entity.email
        )
    }

    fun transform(entities: Iterable<UserInfoEntity>) : List<UserInfoDto>{
        return entities.map { transform(it) }
    }
}