package org.tsdes.jee.jpa.fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 10-Jan-17.
 */
@Entity
public class Foo {

    @Id
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "parent")
    private List<Bar> eagerBi = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "parent")
    private List<Bar> lazyBi = new ArrayList<>();


    public Foo(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Bar> getEagerBi() {
        return eagerBi;
    }

    public void setEagerBi(List<Bar> eagerBi) {
        this.eagerBi = eagerBi;
    }

    public List<Bar> getLazyBi() {
        return lazyBi;
    }

    public void setLazyBi(List<Bar> lazyBi) {
        this.lazyBi = lazyBi;
    }

}
