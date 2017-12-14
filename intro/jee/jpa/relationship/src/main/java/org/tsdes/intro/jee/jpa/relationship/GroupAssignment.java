package org.tsdes.intro.jee.jpa.relationship;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class GroupAssignment {

    @Id //note, not automatically generated
    private Long id;

    private String text;

    //in this case, User is the owner of the relation
    @ManyToMany(mappedBy = "assignments")
    private List<User> authors;


    public GroupAssignment(){}

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

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }
}
