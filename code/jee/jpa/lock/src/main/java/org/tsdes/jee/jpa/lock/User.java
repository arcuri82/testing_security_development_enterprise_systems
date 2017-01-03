package org.tsdes.jee.jpa.lock;

import javax.persistence.*;


@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    @Version
    private Integer version;

    private String name;

    public User(){}

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


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
