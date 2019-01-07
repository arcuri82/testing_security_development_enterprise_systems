package org.tsdes.intro.exercises.quizgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tsdes.intro.exercises.quizgame.entity.MatchStats;
import org.tsdes.intro.exercises.quizgame.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by arcuri82 on 15-Dec-17.
 */
@Service
@Transactional
public class MatchStatsService {

    @Autowired
    private EntityManager em;

    public void reportVictory(String username){

        MatchStats match = getMatchStats(username);

        match.setVictories( 1 + match.getVictories());
    }

    public void reportDefeat(String username){

        MatchStats match = getMatchStats(username);

        match.setDefeats( 1 + match.getDefeats());
    }


    public MatchStats getMatchStats(String username) {

        TypedQuery<MatchStats> query = em.createQuery(
                "select m from MatchStats m where m.user.username=?1", MatchStats.class);
        query.setParameter(1, username);

        List<MatchStats> results = query.getResultList();
        if(!results.isEmpty()){
            return results.get(0);
        }

        User user = em.find(User.class, username);
        if(user == null){
            throw new IllegalArgumentException("No existing user: " + username);
        }

        MatchStats match = new MatchStats();
        match.setUser(user);
        em.persist(match);

        return match;
    }
}
