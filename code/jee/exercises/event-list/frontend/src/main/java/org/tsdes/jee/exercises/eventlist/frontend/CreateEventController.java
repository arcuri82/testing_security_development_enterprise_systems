package org.tsdes.jee.exercises.eventlist.frontend;



import org.tsdes.jee.exercises.eventlist.backend.ejb.EventEjb;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class CreateEventController {

    private String formTitle;
    private String formText;
    private String formCountry;
    private String formLocation;

    @EJB
    private EventEjb eventEjb;
    @Inject
    private LoggingController loggingController;


    public String createEvent(){

        try {
            eventEjb.createEvent(formTitle, formText, formCountry, formLocation, loggingController.getRegisteredUser());
        } catch (Exception e){
            return "newEvent.jsf";
        }

        return "home.jsf";
    }


    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formext) {
        this.formText = formext;
    }

    public String getFormCountry() {
        return formCountry;
    }

    public void setFormCountry(String formCountry) {
        this.formCountry = formCountry;
    }

    public String getFormLocation() {
        return formLocation;
    }

    public void setFormLocation(String formLocation) {
        this.formLocation = formLocation;
    }
}
