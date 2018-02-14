package org.tsdes.intro.spring.testing.mocking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class FooEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private Boolean OK;


    public FooEntity(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getOK() {
        return OK;
    }

    public void setOK(Boolean OK) {
        this.OK = OK;
    }
}
