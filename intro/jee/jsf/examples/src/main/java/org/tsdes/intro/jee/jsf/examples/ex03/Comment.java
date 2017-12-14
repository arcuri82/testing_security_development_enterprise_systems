package org.tsdes.intro.jee.jsf.examples.ex03;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Table(name = "Ex03_Comment")
@Entity
public class Comment {

    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 140;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = MIN_SIZE, max = MAX_SIZE)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


    public Comment(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
