package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;


public class EJB_04_SUPPORTSTest extends TestBase{

    @EJB
    private EJB_04_SUPPORTS ejb;

    @Test
    public void testRead(){

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooWithRequiredTransaction(name);

        assertTrue(queriesEJB.isInDB(name));

        boolean present = ejb.isPresentWithSupports(name);
        assertTrue(present);
    }


    @Test
    public void testFailWrite(){

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        //this will fail, as calling a persist on EntityManager outside of a transaction
        try {
            ejb.createFooWithSupports(name);
            fail();
        } catch (EJBException e){
            //expected
        }
    }


    @Test
    public void testWrite(){

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