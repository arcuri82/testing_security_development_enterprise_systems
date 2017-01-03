package org.tsdes.jee.jsf.examples.ex05.controller;


import org.tsdes.jee.jsf.examples.ex05.ejb.UserEJB;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoggingController implements Serializable{

    @Inject
    private UserEJB userEJB;

    private String formUserName;
    private String formPassword;

    /**
     * The current user registered in this session
     */
    private String registeredUser;


    public LoggingController(){
    }


    public boolean isLoggedIn(){
        return registeredUser != null;
    }

    public String getRegisteredUser(){
        return registeredUser;
    }

    public String logOut(){
        registeredUser = null;
        return "ex05.jsf";
    }


    public String logIn(){
        boolean valid = userEJB.login(formUserName, formPassword);
        if(valid){
            registeredUser = formUserName;
            return "ex05.jsf";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){
        boolean registered = userEJB.createUser(formUserName,formPassword);
        if(registered){
            registeredUser = formUserName;
            return "ex05.jsf";
        } else {
            return "login.jsf";
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
}
