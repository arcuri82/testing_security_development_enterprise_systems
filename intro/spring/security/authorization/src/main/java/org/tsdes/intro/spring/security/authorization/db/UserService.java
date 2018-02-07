package org.tsdes.intro.spring.security.authorization.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;

/**
 * Created by arcuri82 on 13-Dec-17.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;

    public boolean createUser(String username, String password) {

        try {
            String hashedPassword = passwordEncoder.encode(password);

            if (em.find(UserEntity.class, username) == null) {
                return false;
            }

            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setRoles(Collections.singleton("USER"));
            user.setEnabled(true);

            em.persist(user);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
