package org.tsdes.jee.jta.transactions.ejb;

import org.junit.Test;

import javax.ejb.EJBException;

import static org.junit.Assert.*;

public class EJB_08_NOT_SUPPORTEDTest extends TestBase{


    @Test(expected = EJBException.class)
    public void testDirectCall(){
        EJB_08_NOT_SUPPORTED ejb = getEJB(EJB_08_NOT_SUPPORTED.class);

        String name = "abc";

        //write outside of a transaction will fail
        ejb.createFooNotSupported(name);
    }

    @Test
    public void testIndirectCall(){
        EJB_08_NOT_SUPPORTED ejb = getEJB(EJB_08_NOT_SUPPORTED.class);

        String name = "abc";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooIndirectly(name);

        assertTrue(queriesEJB.isInDB(name));
    }


    @Test(expected = EJBException.class)
    public void testIndirectEJB(){
        EJB_08_NOT_SUPPORTED ejb = getEJB(EJB_08_NOT_SUPPORTED.class);

        String name = "abc";

        //even if this method will create a transaction, then its internal call will be
        //outside one due to NOT_SUPPORTED
        ejb.createFooIndirectlyWithEJBCall(name);
    }


    @Test
    public void testIndirectSupports(){
        EJB_08_NOT_SUPPORTED ejb = getEJB(EJB_08_NOT_SUPPORTED.class);

        String name = "abc";

        assertFalse(queriesEJB.isInDB(name));

        //should be fine, as re-using the caller's transaction
        ejb.createFooIndirectlyWithEJBCallWithSupports(name);

        assertTrue(queriesEJB.isInDB(name));
    }


}