package org.tsdes.jee.exercises.mynews.backend.entity;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Comment extends Post {


    @NotNull
    @ManyToOne()
    private Post post;

    @NotNull
    private Boolean moderated;

    public Comment(){
        super();
    }

    @Override
    public int computeScore() {
       int score = super.computeScore();
       if(moderated) {
           score -= 10;
       }
       return score;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Boolean getModerated() {
        return moderated;
    }

    public void setModerated(Boolean moderated) {
        this.moderated = moderated;
    }
}
