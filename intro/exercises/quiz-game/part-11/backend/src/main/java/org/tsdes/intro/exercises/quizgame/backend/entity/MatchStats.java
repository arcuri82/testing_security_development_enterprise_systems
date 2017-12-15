package org.tsdes.intro.exercises.quizgame.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by arcuri82 on 15-Dec-17.
 */
@Entity
public class MatchStats {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne @NotNull
    private User user;

    @Min(0) @NotNull
    private Integer victories = 0 ;

    @Min(0) @NotNull
    private Integer defeats = 0;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVictories() {
        return victories;
    }

    public void setVictories(Integer victories) {
        this.victories = victories;
    }

    public Integer getDefeats() {
        return defeats;
    }

    public void setDefeats(Integer defeats) {
        this.defeats = defeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
