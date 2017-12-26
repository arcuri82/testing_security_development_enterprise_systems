package org.tsdes.intro.jee.jta.transactions.ejb;

import org.junit.Test;

import static org.junit.Assert.*;

public class EJB_05_REQUIRES_NEWTest extends TestBase{

    @Test
    public void testCreateFooRequired() throws Exception {
        EJB_05_REQUIRES_NEW ejb = getEJB(EJB_05_REQUIRES_NEW.class);

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooRequired(name);

        assertTrue(queriesEJB.isInDB(name));
    }

    @Test
    public void testCreateFooRequiresNew() throws Exception {
        EJB_05_REQUIRES_NEW ejb = getEJB(EJB_05_REQUIRES_NEW.class);

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooRequiresNew(name);

        assertTrue(queriesEJB.isInDB(name));
    }


    @Test
    public void testCreateTwoWithRollback() throws Exception {

        EJB_05_REQUIRES_NEW ejb = getEJB(EJB_05_REQUIRES_NEW.class);

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

        EJB_05_REQUIRES_NEW ejb = getEJB(EJB_05_REQUIRES_NEW.class);

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

        EJB_05_REQUIRES_NEW ejb = getEJB(EJB_05_REQUIRES_NEW.class);

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