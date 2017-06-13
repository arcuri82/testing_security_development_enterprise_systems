package org.tsdes.jee.jsf.jacoco.frontend;

import org.tsdes.jee.jsf.jacoco.backend.DataEjb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Controller {

    @EJB
    private DataEjb ejb;

    private Long id;

    private String formText;

    @PostConstruct
    private void init(){
        id = 123L;
        ejb.saveData(id, "No data");
    }

    public String getValue(){
        return ejb.getData(id);
    }

    public void changeData(){
        ejb.saveData(id, formText);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }
}
