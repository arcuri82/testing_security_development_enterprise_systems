package org.tsdes.intro.jee.jpa.embedded;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class User {

    /*
        This class will be mapped to a single table, where each field of these embedded
        objects will be a column, ie

        User
          - name
          - surname
          - city
          - country
          - postcode
     */


    /*
        this is needed when the combination of more than one column should
        be used as key fro the row
     */
    @EmbeddedId
    private UserId id;

    @Embedded
    private Address address;


    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
