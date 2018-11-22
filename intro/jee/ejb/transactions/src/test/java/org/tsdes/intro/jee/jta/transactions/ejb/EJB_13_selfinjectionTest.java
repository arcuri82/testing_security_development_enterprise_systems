package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import javax.ejb.EJBException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Created by arcuri82 on 13-Feb-18.
 */
public class EJB_13_selfinjectionTest extends TestBase{


    @Test
    public void testDirectCall(){

        EJB_13_selfinjection ejb = getEJB(EJB_13_selfinjection.class);

        try{
            //this does not return a boolean, but rather throw an exception
            ejb.createFailDirect();
            fail();
        }catch (EJBException e){
            //expected
        }
    }

    @Test
    public void testIndirect(){

        EJB_13_selfinjection ejb = getEJB(EJB_13_selfinjection.class);

        boolean result = ejb.createFailIndirect();

        assertFalse(result);
    }
}