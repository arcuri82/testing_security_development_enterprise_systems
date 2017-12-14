package org.tsdes.intro.jee.jpa.relationship;

import javax.persistence.*;

@Entity
public class Message {

    @Id @GeneratedValue
    private Long id;

    private String text;


    @OneToOne(fetch = FetchType.LAZY)
    private User destination;


    public Message(){}

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

    public User getDestination() {
        return destination;
    }

    public void setDestination(User destination) {
        this.destination = destination;
    }
}
