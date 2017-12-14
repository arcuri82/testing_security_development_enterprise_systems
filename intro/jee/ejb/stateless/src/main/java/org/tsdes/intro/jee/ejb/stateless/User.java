package org.tsdes.intro.jee.ejb.stateless;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
    Note: default embedded database in GlassFish is Derby, and Derby does not
    like a table being called USER
 */
@Table(name = "user_data")
@Entity
public class User {

    @Id //note, it is not generated
    private String userId;

    private String name;

    private String surname;


    public User(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
