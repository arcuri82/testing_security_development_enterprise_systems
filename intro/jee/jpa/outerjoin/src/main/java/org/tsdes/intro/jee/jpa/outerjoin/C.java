package org.tsdes.intro.jee.jpa.outerjoin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by arcuri82 on 02-Feb-17.
 */
@Entity
public class C {

    @Id
    @GeneratedValue
    private Long id;

    public C() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
