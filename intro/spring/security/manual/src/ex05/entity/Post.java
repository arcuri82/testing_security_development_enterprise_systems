package org.tsdes.intro.jee.jsf.examples.ex05.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = Post.GET_ALL, query = "SELECT p FROM Post p ORDER BY p.creationTime DESC"),
        @NamedQuery(name = Post.DELETE_POST, query = "DELETE FROM Post p WHERE p.id=:id")
})
public class Post {

    public static final String GET_ALL = "Post.get_all";
    public static final String DELETE_POST = "Post.delete_post";

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 1000)
    private String text;

    @NotNull
    @ManyToOne
    private UserDetails author;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;


    public Post() {
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

    public UserDetails getAuthor() {
        return author;
    }

    public void setAuthor(UserDetails author) {
        this.author = author;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
