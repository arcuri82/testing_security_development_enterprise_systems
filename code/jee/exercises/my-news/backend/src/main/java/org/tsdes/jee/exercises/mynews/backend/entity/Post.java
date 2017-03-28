package org.tsdes.jee.exercises.mynews.backend.entity;



import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 1, max = 1024)
    private String text;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @NotNull
    @ManyToOne
    private User author;

    @ElementCollection
    private Set<String> votesFor;

    @ElementCollection
    private Set<String> votesAgainst;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;


    public Post() {
        votesFor = new HashSet<>();
        votesAgainst = new HashSet<>();
        comments = new ArrayList<>();
    }

    public int computeScore() {
        return votesFor.size() - votesAgainst.size();
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<String> getVotesFor() {
        return votesFor;
    }

    public void setVotesFor(Set<String> votesFor) {
        this.votesFor = votesFor;
    }

    public Set<String> getVotesAgainst() {
        return votesAgainst;
    }

    public void setVotesAgainst(Set<String> votesAgainst) {
        this.votesAgainst = votesAgainst;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
