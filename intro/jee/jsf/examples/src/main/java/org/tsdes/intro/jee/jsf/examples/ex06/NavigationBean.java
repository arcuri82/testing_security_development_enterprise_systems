package org.tsdes.intro.jee.jsf.examples.ex06;


import org.tsdes.intro.jee.jsf.examples.ex04.SessionCounter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arcuri82 on 02-Feb-18.
 */
@Named
@RequestScoped
public class NavigationBean {

    @Inject
    private SessionCounter counter;

    public String increaseAndForward(){
        counter.increaseCounter();
        return "ex06_result.xhtml";
    }

    public String increaseAndRedirect(){
        counter.increaseCounter();
        return "ex06_result.xhtml?faces-redirect=true";
    }
}
