package org.tsdes.jee.jpa.outerjoin;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 02-Feb-17.
 */
@Entity
    public class A {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<B> listB = new ArrayList<>();

    public A() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<B> getListB() {
        return listB;
    }

    public void setListB(List<B> listB) {
        this.listB = listB;
    }
}
