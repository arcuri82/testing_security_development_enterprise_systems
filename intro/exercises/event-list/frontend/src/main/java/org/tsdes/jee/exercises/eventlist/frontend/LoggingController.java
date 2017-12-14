package org.tsdes.jee.exercises.eventlist.frontend;




import org.tsdes.jee.exercises.eventlist.backend.Countries;
import org.tsdes.jee.exercises.eventlist.backend.ejb.UserEJB;
import org.tsdes.jee.exercises.eventlist.backend.entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class LoggingController implements Serializable{

    @EJB
    private UserEJB userEJB;

    private String formUserName;
    private String formPassword;
    private String formConfirmPassword;
    private String formFirstName;
    private String formMiddleName;
    private String formLastName;
    private String formCountry;



    /**
     * The current user registered in this session
     */
    private String registeredUser;


    public LoggingController(){
    }

    public String getUserCountry(){
        if(registeredUser == null){
            return null;
        }

        return userEJB.getUser(registeredUser).getCountry();
    }

    public List<String> getCountries(){
        return Countries.getCountries();
    }



    public boolean isLoggedIn(){
        return registeredUser != null;
    }


    public String getRegisteredUser(){
        return registeredUser;
    }

    public User getUser(){
        return userEJB.getUser(registeredUser);
    }

    public String logOut(){
        registeredUser = null;
        return "home.jsf";
    }


    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            registeredUser = formUserName;
            return "home.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){

        if(! formPassword.equals(formConfirmPassword)){
            return "newUser.jsf";
        }

        boolean registered = false;

        try {
            registered = userEJB.createUser(formUserName, formPassword, formFirstName, formMiddleName, formLastName, formCountry);
        } catch (Exception e){
        }

        if(registered){
            registeredUser = formUserName;
            return "home.jsf";
        } else {
            return "newUser.jsf";
        }
    }

    public String getFormUserName() {
        return formUserName;
    }

    public void setFormUserName(String formUserName) {
        this.formUserName = formUserName;
    }

    public String getFormPassword() {
        return formPassword;
    }

    public void setFormPassword(String formPassword) {
        this.formPassword = formPassword;
    }

    public String getFormConfirmPassword() {
        return formConfirmPassword;
    }

    public void setFormConfirmPassword(String formConfirmPassword) {
        this.formConfirmPassword = formConfirmPassword;
    }

    public String getFormFirstName() {
        return formFirstName;
    }

    public void setFormFirstName(String formFirstName) {
        this.formFirstName = formFirstName;
    }

    public String getFormMiddleName() {
        return formMiddleName;
    }

    public void setFormMiddleName(String formMiddleName) {
        this.formMiddleName = formMiddleName;
    }

    public String getFormLastName() {
        return formLastName;
    }

    public void setFormLastName(String formLastName) {
        this.formLastName = formLastName;
    }

    public String getFormCountry() {
        return formCountry;
    }

    public void setFormCountry(String formCountry) {
        this.formCountry = formCountry;
    }
}
