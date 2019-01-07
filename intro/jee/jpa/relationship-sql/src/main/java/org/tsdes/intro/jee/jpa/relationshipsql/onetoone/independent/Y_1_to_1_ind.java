package org.tsdes.intro.jee.jpa.relationshipsql.onetoone.independent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class Y_1_to_1_ind {

    @Id
    @GeneratedValue
    private Long id;

    //note that here we do not have mappedBy
    @OneToOne
    private X_1_to_1_ind x;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public X_1_to_1_ind getX() {
        return x;
    }

    public void setX(X_1_to_1_ind x) {
        this.x = x;
    }
}
