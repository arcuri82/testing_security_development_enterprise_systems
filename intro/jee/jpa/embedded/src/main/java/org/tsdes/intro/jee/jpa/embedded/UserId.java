package org.tsdes.intro.jee.jpa.embedded;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {

    /*
        Btw, usually not a good idea to have the combination name/surname as an id,
        as more than one person can have same name/surname combination
     */

    private String name;
    private String surname;

    public UserId(){}

    /*
        Need both "equals" and "hashCode". These can be automatically generated like the getters/setters
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId = (UserId) o;

        if (name != null ? !name.equals(userId.name) : userId.name != null) return false;
        return surname != null ? surname.equals(userId.surname) : userId.surname == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
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
