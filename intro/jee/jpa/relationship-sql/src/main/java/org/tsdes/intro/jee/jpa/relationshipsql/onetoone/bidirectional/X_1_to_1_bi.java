package org.tsdes.intro.jee.jpa.relationshipsql.onetoone.bidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class X_1_to_1_bi {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Y_1_to_1_bi y;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Y_1_to_1_bi getY() {
        return y;
    }

    public void setY(Y_1_to_1_bi y) {
        this.y = y;
    }
}
