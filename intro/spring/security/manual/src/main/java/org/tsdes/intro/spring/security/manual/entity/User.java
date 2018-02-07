package org.tsdes.intro.spring.security.manual.entity;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "user_table")
public class User {

    @Id
    private String userId;

    /**
     * NEVER store a password in plain text in a database, or anywhere.
     * Rather use an hash combined with a "salt"
     */
    @NotBlank
    private String password;


    public User(){}


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
