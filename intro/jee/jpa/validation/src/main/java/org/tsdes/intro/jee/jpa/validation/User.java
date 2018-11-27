package org.tsdes.intro.jee.jpa.validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;


/*
    Why using constraints? At least 3 major reasons:

    1) if something goes wrong (ie bug in the code), you want things to fail
       as soon as possible (which will help with debugging)

    2) can be used as non-ambiguous documentation

    3) prevent some kinds of malicious attacks (eg consume all hard-drive space)

    For list of available annotations, can look at:
    https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html
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

    /*
        NotBlank means:
        not null, and not empty while ignoring trailing spaces.

        Note: if you do not want to allow spaces, you need to use
        a regular expression
      */
    @NotBlank
    @Size(min = 2 , max = 128)
    private String surname;

     /*
        When you want to represent a date in Java, use classes from java.time.* package,
        like for example ZonedDateTime (Java 8) and not Date (Java <= 7).
        You can consider java.util.Date as deprecated.

        Note: if you are interested in just the date, and not the time,
        you can use LocalDate (like it would be for a birthday date).
        If you are only interested in the time,  use LocalTime.
        If need both and also time zone, use
        ZonedDateTime, because it contains all the following:

        - the date: eg 1/1/2017
        - the time: eg 16:43:23
        - the zone: eg CET (Central European Time) and UTC (Coordinated Universal Time)

        Note: JPA 2.1 (JEE 7) did not support Java 8 time objects, whereas Hibernate did.
        JPA 2.2 (in JEE 8) does support directly them.
     */


    //can't be in the future...
    @Past  // there is a @Future constraint as well
    @NotNull
    private LocalDate dateOfRegistration;


    @Age(min = 18) //this is a custom constraint
    @NotNull
    private LocalDate dateOfBirth;

    /*
        By using a regular expression, we uniquely specify what values the string can have,
        eg in an email we want to say there is at least a @ inside it

        See: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
        for the grammar to use to define regular expressions in Java

        ^  : beginning
        $  : end
        [] : characters, eg any in uppercase [A-Z], or lowercase [0-9]
        +  : one or more times, eg [A-Z]+ means one or more uppercase letters
        *  : zero or several times
        {m,M} : at least m times, but max M

        You might also want to (re-)look at Lesson 09 (Text Search and Regular Expressions)
        from the Algorithms and Data Structures course.

        Note: here it is just an example, as should rather use @Email instead
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

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
