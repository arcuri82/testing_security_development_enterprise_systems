package org.tsdes.intro.jee.jpa.relationshipsql.onetoone.independent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class X_1_to_1_ind {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Y_1_to_1_ind y;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Y_1_to_1_ind getY() {
        return y;
    }

    public void setY(Y_1_to_1_ind y) {
        this.y = y;
    }
}
