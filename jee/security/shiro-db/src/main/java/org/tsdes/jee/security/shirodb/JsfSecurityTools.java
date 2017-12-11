package org.tsdes.jee.security.shirodb;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;


@Named
@RequestScoped
public class JsfSecurityTools {


    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public String userId(){
        Subject subject = getSubject();
        return subject.getPrincipal().toString();
    }

    public void doLogout() throws IOException {

        SecurityUtils.getSubject().logout();

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        externalContext.redirect("login.xhtml?faces-redirect=true");
    }
}
