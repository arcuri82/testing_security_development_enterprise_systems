package org.tsdes.advanced.security.distributedsession.userservice

import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.security.distributedsession.userservice.dto.UserInfoDto


@Repository
interface UserInfoRepository : CrudRepository<UserInfoEntity, String>


@RestController
class RestApi(
        private val crud: UserInfoRepository
) {

    /**
     * Get the number of existing users
     */
    @GetMapping(path = ["/usersInfoCount"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getCount(): ResponseEntity<Long> {

        return ResponseEntity.ok(crud.count())
    }

    /*
        Note: for simplicity here using Entity as DTO...
     */

    @GetMapping(path = ["/usersInfo"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getAll(): ResponseEntity<List<UserInfoDto>> {

        return ResponseEntity.ok(DtoTransformer.transform(crud.findAll()))
    }


    @GetMapping(path = ["/usersInfo/{id}"],
            produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getById(@PathVariable id: String)
            : ResponseEntity<UserInfoDto> {

        val entity = crud.findById(id).orElse(null)
                ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(DtoTransformer.transform(entity))
    }



    @PutMapping(path = ["/usersInfo/{id}"],
            consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun replace(
            @PathVariable id: String,
            @RequestBody dto: UserInfoDto)
            : ResponseEntity<Void> {

        if (id != dto.userId) {
            return ResponseEntity.status(409).build()
        }

        val alreadyExists = crud.existsById(id)
        var code = if(alreadyExists) 204 else 201

        val entity = UserInfoEntity(dto.userId, dto.name, dto.middleName, dto.surname, dto.email)

        try {
            crud.save(entity)
        } catch (e: Exception) {
            code = 400
        }

        return ResponseEntity.status(code).build()
    }
}

