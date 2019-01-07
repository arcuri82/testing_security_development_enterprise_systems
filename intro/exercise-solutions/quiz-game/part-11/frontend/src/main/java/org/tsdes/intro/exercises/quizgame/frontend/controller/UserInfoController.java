package org.tsdes.intro.exercises.quizgame.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.tsdes.intro.exercises.quizgame.backend.entity.MatchStats;
import org.tsdes.intro.exercises.quizgame.backend.service.MatchStatsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arcuri82 on 13-Dec-17.
 */
@Named
@RequestScoped
public class UserInfoController {

    @Autowired
    private MatchStatsService matchStatsService;

    public String getUserName(){
        return ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public MatchStats getStats(){
        return matchStatsService.getMatchStats(getUserName());
    }
}
