package org.tsdes.intro.jee.jta.transactions.ejb;

import org.junit.jupiter.api.Test;

import javax.ejb.EJBException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class EJB_04_SUPPORTSTest extends TestBase{


    @Test
    public void testRead(){

        EJB_04_SUPPORTS ejb = getEJB(EJB_04_SUPPORTS.class);

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooWithRequiredTransaction(name);

        assertTrue(queriesEJB.isInDB(name));

        boolean present = ejb.isPresentWithSupports(name);
        assertTrue(present);
    }


    @Test
    public void testFailWrite(){

        EJB_04_SUPPORTS ejb = getEJB(EJB_04_SUPPORTS.class);

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

        EJB_04_SUPPORTS ejb = getEJB(EJB_04_SUPPORTS.class);

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