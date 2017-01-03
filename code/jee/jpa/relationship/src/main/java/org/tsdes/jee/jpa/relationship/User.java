package org.tsdes.jee.jpa.relationship;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String surname;

    @OneToOne(fetch = FetchType.EAGER) //Eager is the default for OneToOne, so not needed.
    //Address has its own table, and here in User we just keep a foreign key.
    //Note: Address does not have a link back to User, ie relation is unidirectional
    private Address address;


    //this OneToOne is bidirectional, but no need of any special setting here
    @OneToOne
    private AddressWithUserLink addressWithUserLink;


    @OneToMany(fetch = FetchType.LAZY) //Lazy is the default here, as data structure might be big
    private List<Message> sentMessages;


    //bidirectional. operations (eg persist, update) on this User will be cascaded to all these messages
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.ALL)
    private List<MessageWithUserLink> sentMessagesWithSenderLink;

    @ManyToMany
    //could have used here a List
    private Map<Long, GroupAssignment> assignments;



    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Long, GroupAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<Long, GroupAssignment> assignments) {
        this.assignments = assignments;
    }

    public List<MessageWithUserLink> getSentMessagesWithSenderLink() {
        return sentMessagesWithSenderLink;
    }

    public void setSentMessagesWithSenderLink(List<MessageWithUserLink> sentMessagesWithSenderLink) {
        this.sentMessagesWithSenderLink = sentMessagesWithSenderLink;
    }


    public AddressWithUserLink getAddressWithUserLink() {
        return addressWithUserLink;
    }

    public void setAddressWithUserLink(AddressWithUserLink addressWithUserLink) {
        this.addressWithUserLink = addressWithUserLink;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
