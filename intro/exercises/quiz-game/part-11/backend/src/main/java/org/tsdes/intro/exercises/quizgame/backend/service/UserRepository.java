package org.tsdes.intro.exercises.quizgame.backend.service;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by arcuri82 on 13-Dec-17.
 */
public interface UserRepository extends CrudRepository<UserEntity, String> {
}
