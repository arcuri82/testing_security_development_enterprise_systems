package org.tsdes.intro.jee.jpa.relationshipsql.onetomany.joincolumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
@Entity
public class X_1_to_m_join {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    // this means that in Y there will be a FK called with name, which is pointing
    // back to X
    @JoinColumn(name = "foo_column")
    private List<Y_1_to_m_join> ys = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Y_1_to_m_join> getYs() {
        return ys;
    }

    public void setYs(List<Y_1_to_m_join> ys) {
        this.ys = ys;
    }
}
