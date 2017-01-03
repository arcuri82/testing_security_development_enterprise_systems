package org.tsdes.jee.exercises.eventlist.backend.entity;

import org.tsdes.jee.exercises.eventlist.backend.validation.Country;
import org.tsdes.jee.exercises.eventlist.backend.validation.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = Event.GET_ALL, query = "SELECT e FROM Event e"),
        @NamedQuery(name = Event.GET_COUNTRY, query = "SELECT e FROM Event e WHERE e.country = :fcountry")
})
public class Event {

    public static final String GET_ALL = "EVENT_GET_ALL";
    public static final String GET_COUNTRY = "EVENT_GET_COUNTRY";

    @Id @GeneratedValue
    private Long id;

    @NotEmpty @Size(min=1, max=50)
    private String title;

    @NotEmpty @Size(min=1, max=1024)
    private String text;

    @NotEmpty @Size(min=1, max=32)
    private String author;

    @Country
    private String country;

    @NotEmpty @Size(min=1, max=256)
    private String location;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> attendingUsers;


    public List<User> getAttendingUsers() {
        if(attendingUsers == null){
            return new ArrayList<>();
        }
        return attendingUsers;
    }

    public void setAttendingUsers(List<User> attendingUsers) {
        this.attendingUsers = attendingUsers;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
