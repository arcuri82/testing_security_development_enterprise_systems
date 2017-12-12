package org.tsdes.jee.security.shirodb;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by arcuri82 on 08-Dec-17.
 */
@Stateless
public class UserEjb {

    @PersistenceContext
    private EntityManager em;


    private PasswordService passwordService = new DefaultPasswordService();

    public void createUser(String name, String password){

        UserEntity user = new UserEntity();
        user.setUsername(name);
        user.setPassword(passwordService.encryptPassword(password));

        em.persist(user);
    }

    public UserEntity findUser(String name){
        return em.find(UserEntity.class, name);
    }

    public boolean login(String username, String password){

        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, false));
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
