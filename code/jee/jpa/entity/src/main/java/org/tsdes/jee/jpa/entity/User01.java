package org.tsdes.jee.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//this class will be mapped to a table in the database with same name
@Entity
public class User01 {

    //need a unique id (ie, the Primary Key) for this row. value is not important, and we let the DB to generate it
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String surname;

    //each entity needs an empty default-constructor
    public User01(){}

    /*
        Note the following getters/setters can be automatically generated, in both
        IntelliJ and Eclipse.
        In IntelliJ, right-click, choose "Generate..." and then "Getter and Setter"
     */

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
