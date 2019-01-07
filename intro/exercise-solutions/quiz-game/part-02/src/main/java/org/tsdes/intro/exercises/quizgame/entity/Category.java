package org.tsdes.intro.exercises.quizgame.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 30-Nov-16.
 */
@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<SubCategory> subCategories;

    public Category() {
        subCategories = new ArrayList<>();
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
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
