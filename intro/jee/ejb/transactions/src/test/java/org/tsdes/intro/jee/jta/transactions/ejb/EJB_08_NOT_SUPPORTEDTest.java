package org.tsdes.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;


public class EJB_08_NOT_SUPPORTEDTest extends TestBase{

    @EJB
    private EJB_08_NOT_SUPPORTED ejb;

    @Test
    public void testDirectCall(){

        String name = "abc";

        //write outside of a transaction will fail
        try {
            ejb.createFooNotSupported(name);
            fail();
        }catch (EJBException e){
            //expected
        }
    }

    @Test
    public void testIndirectCall(){

        String name = "abc";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooIndirectly(name);

        assertTrue(queriesEJB.isInDB(name));
    }


    @Test
    public void testIndirectEJB(){

        String name = "abc";

        //even if this method will create a transaction, then its internal call will be
        //outside one due to NOT_SUPPORTED
        try {
            ejb.createFooIndirectlyWithEJBCall(name);
            fail();
        }catch (EJBException e){
            //expected
        }
    }


    @Test
    public void testIndirectSupports(){

        String name = "abc";

        assertFalse(queriesEJB.isInDB(name));

        //should be fine, as re-using the caller's transaction
        ejb.createFooIndirectlyWithEJBCallWithSupports(name);

        assertTrue(queriesEJB.isInDB(name));
    }


}