package org.tsdes.jee.jpa.relationship;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
public class MessageWithUserLink extends Message {

    @ManyToOne
    private User sender;

    public MessageWithUserLink(){}

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
