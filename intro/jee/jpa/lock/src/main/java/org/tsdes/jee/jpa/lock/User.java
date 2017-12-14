package org.tsdes.jee.jpa.lock;

import javax.persistence.*;


@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    /*
        Besides having a field marked with @Version, there are other
        options for optimistic locking.

        You can use org.hibernate.annotations.OptimisticLocking to choose
        existing fields for the update/change checks.
        However, that is a special feature of Hibernate, and not part
        of the JPA 2.1 standard
     */
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
