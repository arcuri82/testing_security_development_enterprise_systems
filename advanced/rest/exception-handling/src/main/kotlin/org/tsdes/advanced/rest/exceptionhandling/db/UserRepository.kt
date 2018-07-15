package org.tsdes.advanced.rest.exceptionhandling.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository  : CrudRepository<UserEntity, Long>