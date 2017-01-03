package org.tsdes.jee.jpa.lock;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class CounterWithVersion implements Counter {

    @Version
    private Integer version;

    @Id
    @GeneratedValue
    private Long id;

    private int counter;


    public CounterWithVersion(){}

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
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
