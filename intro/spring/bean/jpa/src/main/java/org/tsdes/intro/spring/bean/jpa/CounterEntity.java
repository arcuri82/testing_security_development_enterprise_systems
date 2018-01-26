package org.tsdes.intro.spring.bean.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@Entity
public class CounterEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    @Min(0)
    private Long value;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
