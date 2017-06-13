package org.tsdes.jee.jpa.lock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BaseCounter implements Counter{

    @Id @GeneratedValue
    private Long id;

    private int counter;

    public BaseCounter(){}

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
