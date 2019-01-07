package org.tsdes.intro.jee.jpa.relationshipsql.onetomany.joincolumn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class Y_1_to_m_join {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
