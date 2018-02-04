package org.tsdes.intro.spring.testing.coverage.jacoco.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.tsdes.intro.spring.testing.coverage.jacoco.backend.DataService;
import org.tsdes.intro.spring.testing.coverage.jacoco.backend.DefaultDataValue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Controller {

    @Autowired
    private DataService service;

    private Long id;

    private String formText;

    @PostConstruct
    private void init(){
        id = 123L;
        service.saveData(id, DefaultDataValue.get());
    }

    public String getValue(){
        return service.getData(id);
    }

    public void changeData(){
        service.saveData(id, formText);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }
}
