package org.tsdes.intro.spring.security.manual.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.tsdes.intro.spring.security.manual.service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable{

    @Autowired
    private UserService userService;

    private String formUserName;
    private String formPassword;

    /**
     * The current user registered in this session
     */
    private String registeredUser;


    public LoginController(){
    }


    public boolean isLoggedIn(){
        return registeredUser != null;
    }

    public String getRegisteredUser(){
        return registeredUser;
    }

    public String logOut(){
        registeredUser = null;
        return "index.jsf";
    }


    public String logIn(){
        boolean valid = userService.login(formUserName, formPassword);
        if(valid){
            registeredUser = formUserName;
            return "index.jsf?faces-redirect=true";
        } else {
            return "login.jsf";
        }
    }

    public String registerNew(){

        boolean registered = false;
        try {
            registered = userService.createUser(formUserName, formPassword);
        }catch (Exception e){
            //nothing to do
        }

        if(registered){
            registeredUser = formUserName;
            return "index.jsf?faces-redirect=true";
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
