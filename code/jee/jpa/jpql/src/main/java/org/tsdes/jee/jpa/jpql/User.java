package org.tsdes.jee.jpa.jpql;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
    If you are using IntelliJ, and you get errors in the query string, make sure
    that in "File -> Project Structure -> Facets -> JPA" the "Default JPA Provider"
    is set to Hibernate, and also add the "persistence.xml" file in the Facets's JPA
    configuration
 */
@NamedQueries({
        @NamedQuery(name = User.GET_ALL, query = "select u from User u"),
        @NamedQuery(name = User.GET_ALL_IN_NORWAY, query = "select u from User u where u.address.country = 'Norway' ")
})
@Entity
public class User {

    //it is good to have the query names as public static final fields, so they can be accessed by the callee
    public static final String GET_ALL = "GET_ALL";
    public static final String GET_ALL_IN_NORWAY = "GET_ALL_IN_NORWAY";

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Address address;

    @ManyToMany
    private List<User> friends;


    public User(){}

    //note how this getter has been modified
    public List<User> getFriends() {
        if(friends == null){
            friends = new ArrayList<>();
        }
        return friends;
    }

    public Address getAddress() {
        if(address == null){
            address = new Address();
        }
        return address;
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

    public void setAddress(Address address) {
        this.address = address;
    }



    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
