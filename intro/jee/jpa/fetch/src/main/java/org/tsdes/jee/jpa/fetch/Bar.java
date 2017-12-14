package org.tsdes.jee.jpa.fetch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by arcuri82 on 10-Jan-17.
 */
@Entity
public class Bar {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Foo parent;


    public Bar(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Foo getParent() {
        return parent;
    }

    public void setParent(Foo parent) {
        this.parent = parent;
    }
}
