package org.tsdes.intro.jee.jpa.relationship;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AddressWithUserLink extends Address {

    //all fields of Address are inherited, included @Id

    @OneToOne(mappedBy = "addressWithUserLink")
    //User is the owner of this bidirectional relation, so need
    //to specify which field in the User table is mapping this address
    private User user;

    public AddressWithUserLink(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
