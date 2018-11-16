package org.tsdes.intro.jee.jpa.validation;



import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.ZonedDateTime;

/**
 * In JEE 7, @NotBlank and @Email were available in Hibernate, but not JPA.
 * You could still use them, but not with other providers besides Hibernate.
 * But, because they are quite usuful, in JEE 8 those "not-standard" constraints
 * were added to JPA.
 *
 * Created by arcuri82 on 25-Jan-17.
 */
@Entity
public class NotStandard {

    @Id
    @GeneratedValue
    private Long id;

    /*
        The problem with this is that a string like "   " with
        just empty spaces would pass the constraint
     */
    @NotNull
    @Size(min = 2 , max = 128)
    private String  name;

    /*
        NotBlank means:
        not null, and not empty while ignoring trailing spaces.

        Note: if you do not want to allow spaces, you need to use
        a regular expression
      */
    @NotBlank
    @Length(min=2, max = 128)
    private String surname;

    /*
        When you want to represent a date in Java, use
        ZonedDateTime (Java 8) and not Date (Java <= 7)

        Note: if you are interested in just the date, and not the time,
        you can use LocalDate (like it would be for a birthday date).
        If you are only interested in the time,  use LocalTime.
        However, to be on the safe side, if you are unsure, go for
        ZonedDateTime, because it contains all the following:

        - the date: eg 1/1/2017
        - the time: eg 16:43:23
        - the zone: eg CET (Central European Time) and UTC (Coordinated Universal Time)

        Note: JPA 2.1 does not support Java 8 time objects, but Hibernate does
     */
    @Past
    private ZonedDateTime dateOfBirth;

    //another custom constraint from Hibernate
    @Email
    private String email;

    public NotStandard() {
    }

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

    public ZonedDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(ZonedDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
