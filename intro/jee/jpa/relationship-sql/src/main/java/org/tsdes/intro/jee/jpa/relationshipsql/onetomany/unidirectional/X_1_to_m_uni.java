package org.tsdes.intro.jee.jpa.relationshipsql.onetomany.unidirectional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class X_1_to_m_uni {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Y_1_to_m_uni> ys = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Y_1_to_m_uni> getYs() {
        return ys;
    }

    public void setYs(List<Y_1_to_m_uni> ys) {
        this.ys = ys;
    }
}
