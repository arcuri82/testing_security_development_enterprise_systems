package org.tsdes.intro.jee.ejb.time.datalayer;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Comment{

    @Id @GeneratedValue
    private Long id;

    @NotNull @Size(max = 1000)
    private String text;

    @NotNull @Size(max = 100)
    private String author;


    public Comment(){
    }

    public Comment(Long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
