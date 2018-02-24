package org.tsdes.intro.spring.flyway;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class First {


    @Id
    private Long id;


    private String data;

    private Integer addedField;


    public First(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getAddedField() {
        return addedField;
    }

    public void setAddedField(Integer addedField) {
        this.addedField = addedField;
    }
}
