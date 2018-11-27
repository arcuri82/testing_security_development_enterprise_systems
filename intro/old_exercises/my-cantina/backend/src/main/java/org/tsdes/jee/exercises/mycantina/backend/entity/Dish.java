package org.tsdes.jee.exercises.mycantina.backend.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Created by arcuri82 on 22-May-17.
 */
@Entity
public class Dish {

    @Id
    @GeneratedValue
    private Long id;


    @NotBlank
    @Size(min = 1, max = 1024)
    private String name;


    @NotBlank
    @Size(min = 1, max = 4096)
    private String description;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
