package org.tsdes.intro.jee.ejb.callback;

import javax.ejb.Stateless;

@Stateless
public class B {

    public String getValue(){
        return "Called b.getValue()";
    }
}
