package org.tsdes.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EJB_05_REQUIRES_NEWTest extends TestBase{

    @EJB
    private EJB_05_REQUIRES_NEW ejb;

    @Test
    public void testCreateFooRequired() throws Exception {
        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        //no transaction? REQUIRED will create a new one
        ejb.createFooRequired(name);

        assertTrue(queriesEJB.isInDB(name));
    }

    @Test
    public void testCreateFooRequiresNew() throws Exception {

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        /*
            REQUIRES_NEW will always create a new transaction.
            but, as here there was no ongoing transaction, its behavior
            is equivalent to REQUIRED
         */
        ejb.createFooRequiresNew(name);

        assertTrue(queriesEJB.isInDB(name));
    }


    @Test
    public void testCreateTwoWithRollback() throws Exception {

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwoWithRollback(first, second);

        assertFalse(queriesEJB.isInDB(first)); //failed because rollback
        assertFalse(queriesEJB.isInDB(second));//still failed, as REQUIRES_NES was not in a EJB proxy call
    }

    @Test
    public void testCreateTwoWithRollbackInEJBCall() throws Exception {

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwoWithRollbackInEJBCall(first, second);

        assertFalse(queriesEJB.isInDB(first)); //failed because rollback
        assertTrue(queriesEJB.isInDB(second)); //rollback no effect here
    }


    @Test
    public void testCreateTwoWithRollbackInEJBCallOnSameTransaction() throws Exception {

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwoWithRollbackInEJBCallOnSameTransaction(first, second);

        //both failed due to rollback
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));
    }
}