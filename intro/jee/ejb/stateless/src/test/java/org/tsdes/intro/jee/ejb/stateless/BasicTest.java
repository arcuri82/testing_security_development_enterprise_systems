package org.tsdes.intro.jee.ejb.stateless;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicTest {

    @Test
    public void testItWrong(){

        Basic basic = new Basic();
        assertNotNull(basic.getValue());

        /*
            If you instantiate a bean directly, it is just a Java class,
            and not a EJB. All the functionalities added by the container
            would not be present.
            In this particular case, it works just because we do not do
            anything needing a container.
         */
    }
}