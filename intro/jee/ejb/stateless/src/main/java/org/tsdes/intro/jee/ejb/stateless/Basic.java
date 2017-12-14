package org.tsdes.intro.jee.ejb.stateless;


import javax.ejb.Stateless;

/*
    There are three types of Enterprise Java Bean (EJB):
    - Stateless
    - Stateful
    - Singleton

    To make a class a EJB, just need to tag it with the
    corresponding annotation
 */

@Stateless
public class Basic {

    /*
        EJB are "managed" by the container (eg, Wildfly or GlassFish).
        This mainly implies:
        - dependency injection
        - special handling (eg automated transactions)

        this class is trivial, and needs neither
     */

    public String getValue(){
        return "something";
    }
}
