package org.tsdes.jee.jta.transactions.ejb;

import org.junit.Test;

import javax.ejb.EJBException;

import static org.junit.Assert.*;

public class EJB_04_SUPPORTSTest extends TestBase{


    @Test
    public void testRead(){

        EJB_04_SUPPORTS ejb = getEJB(EJB_04_SUPPORTS.class);

        String name = "pg5100";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooWithRequiredTransaction(name);

        assertTrue(queriesEJB.isInDB(name));

        boolean present = ejb.isPresentWithSupports(name);
        assertTrue(present);
    }


    @Test(expected = EJBException.class)
    public void testFailWrite(){

        EJB_04_SUPPORTS ejb = getEJB(EJB_04_SUPPORTS.class);

        String name = "pg5100";

        assertFalse(queriesEJB.isInDB(name));

        //this will fail, as calling a persist on EntityManager outside of a transaction
        ejb.createFooWithSupports(name);
    }


    @Test
    public void testWrite(){

        EJB_04_SUPPORTS ejb = getEJB(EJB_04_SUPPORTS.class);

        String name = "pg5100";

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwo(first,second);

        //should work, as SUPPORTS method was called from within a transaction
        assertTrue(queriesEJB.isInDB(first));
        assertTrue(queriesEJB.isInDB(second));
    }
}