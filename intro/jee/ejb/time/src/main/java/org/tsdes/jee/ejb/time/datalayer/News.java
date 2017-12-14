package org.tsdes.jee.ejb.time.datalayer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = News.GET_ALL, query = "SELECT n FROM News n")
public class News implements Serializable {

    public static final String GET_ALL = "get_all";

    @Id @GeneratedValue
    private Long id;

    @NotNull @Size(max = 1000)
    private String text;

    @NotNull @Size(max = 100)
    private String author;

    @OneToMany(targetEntity=Comment.class, fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments;


    public News(){
    }

    public News(Long id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.comments = new ArrayList<>();
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
