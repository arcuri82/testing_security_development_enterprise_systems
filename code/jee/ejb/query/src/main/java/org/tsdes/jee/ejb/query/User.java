package org.tsdes.jee.ejb.query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "query_User")
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;


    private String name;

    @OneToMany
    private List<Comment> comments;

    @OneToMany
    private List<Post> posts;

    /*
        Constraint:  counter == comments.size() + posts.size()

        Keeping a counter of total number of posts/comments has few disadvantages:
        - need to modify database schema, and so not scalable (what if need new statistics?)
        - take extra space
        - have to make sure the counter is always updated

        But it does have a positive side:
        - if it is very used often, then might be more efficient instead of re-calculating each time
     */
    private int counter;


    public User() {
        comments = new ArrayList<>();
        posts = new ArrayList<>();
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
