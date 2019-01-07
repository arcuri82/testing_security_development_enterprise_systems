package org.tsdes.intro.exercises.quizgame.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by arcuri82 on 30-Nov-16.
 */
@Entity
public class SubCategory {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Category parent;

    public SubCategory() {
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
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
