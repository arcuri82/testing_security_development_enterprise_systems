package org.tsdes.intro.exercises.quizgame.frontend.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Created by arcuri82 on 13-Dec-17.
 */
@Named
@RequestScoped
public class UserInfoController {

    public String getUserName(){
        return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
