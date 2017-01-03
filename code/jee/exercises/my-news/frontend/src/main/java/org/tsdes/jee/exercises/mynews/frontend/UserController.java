package org.tsdes.jee.exercises.mynews.frontend;

import org.tsdes.jee.exercises.mynews.backend.ejb.UserEJB;
import org.tsdes.jee.exercises.mynews.backend.entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class UserController {

    @EJB
    private UserEJB userEJB;

    public User getUser(String userId){

        return userEJB.getUser(userId);
    }

    public long getKarma(String userId){
        return  userEJB.computeKarma(userId);
    }
}
