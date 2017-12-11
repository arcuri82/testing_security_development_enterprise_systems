package org.tsdes.jee.security.shirodb;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by arcuri82 on 08-Dec-17.
 */
@Entity(name="USERS")
public class UserEntity {

    @Id
    @NotNull
    @Max(255)
    private String username;

    @NotNull
    @Max(255)
    private String password;

    @ElementCollection
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name="username"))
    @Column(name="role_name")
    private List<String>  user_roles;


    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getUser_roles() {
        return user_roles;
    }

    public void setUser_roles(List<String> user_roles) {
        this.user_roles = user_roles;
    }
}
