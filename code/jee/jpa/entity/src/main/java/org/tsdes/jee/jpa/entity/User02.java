package org.tsdes.jee.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class User02 {

    //only difference from User01 is that here @GeneratedValue is missing
    @Id
    private Long id;

    private String name;
    private String surname;

    public User02(){}

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
