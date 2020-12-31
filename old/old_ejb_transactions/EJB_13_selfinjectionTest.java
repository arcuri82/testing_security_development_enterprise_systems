package org.tsdes.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


/**
 * Created by arcuri82 on 13-Feb-18.
 */
public class EJB_13_selfinjectionTest extends TestBase{

    @EJB
    private EJB_13_selfinjection ejb;

    @Test
    public void testDirectCall(){

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

        boolean result = ejb.createFailIndirect();

        assertFalse(result);
    }
}