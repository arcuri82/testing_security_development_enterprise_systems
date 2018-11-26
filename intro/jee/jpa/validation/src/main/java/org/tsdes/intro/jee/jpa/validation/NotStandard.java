package org.tsdes.intro.jee.jpa.validation;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * In JEE 7, @NotBlank and @Email were available in Hibernate, but not JPA.
 * You could still use them, but not with other providers besides Hibernate.
 * But, because they are quite useful, in JEE 8 those "not-standard" constraints
 * were added to JPA.
 *
 * But there are still some constraints defined in Hibernate which are not in JPA.
 * See for example:
 * https://docs.jboss.org/hibernate/validator/5.1/api/org/hibernate/validator/constraints/package-summary.html
 *
 * Created by arcuri82 on 25-Jan-17.
 */
@Entity
public class NotStandard {

    @Id
    @GeneratedValue
    private Long id;

    /*
       @Length (from Hibernate) is equivalent of @Size (in JPA).
       Better to use @Size.
     */
    @NotBlank
    @Length(min=2, max = 128)
    private String  name;

    /*
        This was custom in Hibernate, but now part of JPA 2.2 as well
     */
    @Email
    private String email;

    /*
        A URL has a well defined structure. See:
        https://tools.ietf.org/html/rfc3986
     */
    @URL
    private String homePage;

    /**
     * A range between a min and a max value.
     *
     * Note that JPA has @Min and @Max annotations as well
     */
    @Range(min = 0, max = 150)
    private Integer ageInYears;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public Integer getAgeInYears() {
        return ageInYears;
    }

    public void setAgeInYears(Integer ageInYears) {
        this.ageInYears = ageInYears;
    }
}
