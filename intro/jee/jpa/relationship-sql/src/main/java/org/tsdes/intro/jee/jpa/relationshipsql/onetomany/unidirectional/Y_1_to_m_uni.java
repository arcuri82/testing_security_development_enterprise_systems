package org.tsdes.intro.jee.jpa.relationshipsql.onetomany.unidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class Y_1_to_m_uni {

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
