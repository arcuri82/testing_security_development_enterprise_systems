package org.tsdes.intro.spring.deployment;

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

    @Id
    private String id;

    @NotNull
    @Min(0)
    private Long value;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
