package org.tsdes.intro.exercises.quizgame.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by arcuri82 on 30-Nov-16.
 */
@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Category() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
