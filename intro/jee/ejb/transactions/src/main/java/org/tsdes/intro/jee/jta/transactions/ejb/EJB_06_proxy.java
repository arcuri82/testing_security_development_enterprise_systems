package org.tsdes.intro.jee.jta.transactions.ejb;


import javax.ejb.Stateless;
import java.lang.reflect.Method;

@Stateless
public class EJB_06_proxy {

    public String getClassNameFromEJBInstance(){
        return this.getClass().getName();
    }

    public Method[] getMethodsFromEJBInstance(){
        return this.getClass().getDeclaredMethods();
    }
}
