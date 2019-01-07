package org.tsdes.intro.jee.jpa.relationshipsql.onetomany.bidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class X_1_to_m_bi {

    @Id
    @GeneratedValue
    private Long id;

    /*
        In a bidirectional relationship, the owner is always the one
        with @ManyToOne
     */
    @OneToMany(mappedBy = "x")
    private List<Y_1_to_m_bi> ys = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Y_1_to_m_bi> getYs() {
        return ys;
    }

    public void setYs(List<Y_1_to_m_bi> ys) {
        this.ys = ys;
    }
}
