package org.tsdes.intro.jee.jpa.validation;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;


/*
    Why using constraints? At least 3 major reasons:

    1) if something goes wrong (ie bug in the code), you want things to fail
       as soon as possible (which will help with debugging)

    2) can be used as non-ambiguous documentation

    3) prevent some kinds of malicious attacks (eg consume all hard-drive space)
 */

@UserClassConstraints //custom constraint
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    /*
        This states the name should always exist, and be at least
        two letters.
        It is always important to fix an upper-bound, 128 in this case,
        especially when the data come from user inputs... eg, you want
        to avoid a malicious attacker starting to register new users having
        names that are long 2 billion characters (MAX_INT), taking each one
        2GB of space in your database...
     */
    @NotNull
    @Size(min = 2 , max = 128)
    private String name;

    //the middle name might be missing, but, if there is one, can't be too long
    @Size(min = 0 , max = 128)
    private String middleName;

    @NotNull
    @Size(min = 2 , max = 128)
    private String surname;

    //can't be in the future...
    @Past  // there is a @Future constraint as well
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateOfRegistration;

    /*
       unfortunately, Date is Java <= 7, and the one supported in JPA 2.1.
       to handle the new Java 8 Time API (java.time.*) we would need special mappings
       or use non-standard (ie no JPA) functionalities in Hibernate

       http://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
       http://www.thoughts-on-java.org/hibernate-5-date-and-time/
     */


    @Age(min = 18) //this is a custom constraint
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    /*
        By using a regular expression, we uniquely specify what values the string can have,
        eg in an email we want to say there is at least a @ inside it

        See: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
        for the grammar to use to define regular expressions in Java

        See: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

        ^  : beginning
        $  : end
        [] : characters, eg any in uppercase [A-Z], or lowercase [0-9]
        +  : one or more times, eg [A-Z]+ means one or more uppercase letters
        *  : zero or several times
        {m,M} : at least m times, but max M
     */
    @NotNull
    @Column(unique=true)
    @Pattern(regexp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    public User(){}

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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
