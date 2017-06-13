package org.tsdes.jee.ejb.arquillian;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Data {

    @Id
    private Long id;

    private Integer value;

    public Data(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
