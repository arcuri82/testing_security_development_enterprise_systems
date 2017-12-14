package org.tsdes.intro.exercises.quizgame.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Created by arcuri82 on 13-Dec-17.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userCrud;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean createUser(String username, String password) {

        try {
            String hashedPassword = passwordEncoder.encode(password);

            if (userCrud.exists(username)) {
                return false;
            }

            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setRoles(Collections.singleton("USER"));
            user.setEnabled(true);

            userCrud.save(user);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
