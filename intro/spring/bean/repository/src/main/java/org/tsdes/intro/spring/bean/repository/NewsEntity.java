package org.tsdes.intro.spring.bean.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * Created by arcuri82 on 25-Feb-19.
 */
@Entity
public class NewsEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 32)
    private String authorId;

    @NotBlank @Size(max = 1024)
    private String text;

    @NotBlank
    private String country;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

