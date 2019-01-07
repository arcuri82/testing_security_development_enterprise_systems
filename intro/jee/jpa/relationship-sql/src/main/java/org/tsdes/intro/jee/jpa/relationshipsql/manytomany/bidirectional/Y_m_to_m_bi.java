package org.tsdes.intro.jee.jpa.relationshipsql.manytomany.bidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class Y_m_to_m_bi {

    @Id
    @GeneratedValue
    private Long id;

    //here we are stating that X is the owner
    @ManyToMany(mappedBy = "ys")
    private List<X_m_to_m_bi> xs = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<X_m_to_m_bi> getXs() {
        return xs;
    }

    public void setXs(List<X_m_to_m_bi> xs) {
        this.xs = xs;
    }
}
