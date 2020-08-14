package org.tsdes.advanced.exercises.cardgame.usercollections

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.exercises.cardgame.usercollections.db.UserService
import org.tsdes.advanced.exercises.cardgame.usercollections.dto.UserDto


@Api(value = "/api/user-collections", description = "Operations on card collections owned by users")
@RequestMapping(
        path = ["/api/user-collections"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class RestAPI(
        private val userService: UserService
) {

    @ApiOperation("Retrieve card collection information for a specific user")
    @GetMapping(path = ["/{userId}"])
    fun getUserInfo(
            @PathVariable("userId") userId: String
    ) : ResponseEntity<UserDto>{

        val user = userService.findByIdEager(userId)
        if(user == null){
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.status(200).body(DtoConverter.transform(user))
    }

    @ApiOperation("Create a new user, with the given id")
    @PutMapping(path = ["/{userId}"])
    fun createUser(
            @PathVariable("userId") userId: String
    ): ResponseEntity<Void>{
        val ok = userService.registerNewUser(userId)
        return if(!ok)  ResponseEntity.status(400).build()
            else ResponseEntity.status(201).build()
    }

}