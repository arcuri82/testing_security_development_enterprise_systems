package org.tsdes.intro.jee.jpa.embedded;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Embeddable objects used as IDs must implement Serializable, otherwise you will
 * get a runtime exception. For a reason you can read:
 * https://stackoverflow.com/questions/9271835/why-composite-id-class-must-implement-serializable
 *
 * "Serializable" means that the object can be converted to a bit-string and saved on disk.
 * By default, Java objects are not serializable, unless explicitly implementing the Serializable
 * interface.
 * All object fields must be Serializable as well (note that String does implement Serializable).
 * If some fields should be skipped, can use "transient" keyword.
 *
 * Note: servers might need to store objects locally (eg on hard-drive).
 * However, sending serialized objects over the network is a security risk (there has been a lot
 * of vulnerabilities about it).
 * In such case, an object should rather be unmarshalled into formats like JSON and XML.
 */
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
        return Objects.equals(name, userId.name) &&
                Objects.equals(surname, userId.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
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
