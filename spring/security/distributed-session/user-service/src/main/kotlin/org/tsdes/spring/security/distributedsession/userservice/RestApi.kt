package org.tsdes.spring.security.distributedsession.userservice

import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*


@Repository
interface UserInfoRepository : CrudRepository<UserInfoEntity, String>


@RestController
@RequestMapping(path = arrayOf("/usersInfo"))
class RestApi(
        private val crud: UserInfoRepository
) {

    /*
        Note: for simplicity here using Entity as DTO...
     */

    @GetMapping(produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getAll(): ResponseEntity<List<UserInfoEntity>> {

        return ResponseEntity.ok(crud.findAll().toList())
    }


    @GetMapping(path = arrayOf("/{id}"),
            produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getById(@PathVariable id: String)
            : ResponseEntity<UserInfoEntity> {

        val entity = crud.findOne(id)
                ?: return ResponseEntity.status(404).build()

        return ResponseEntity.ok(entity)
    }



    @PutMapping(path = arrayOf("/{id}"),
            consumes = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun replace(
            @PathVariable id: String,
            @RequestBody dto: UserInfoEntity)
            : ResponseEntity<Void> {

        if (id != dto.userId) {
            return ResponseEntity.status(409).build()
        }

        val alreadyExists = crud.exists(id)
        var code = if(alreadyExists) 204 else 201

        try {
            crud.save(dto)
        } catch (e: Exception) {
            code = 400
        }

        return ResponseEntity.status(code).build()
    }
}

