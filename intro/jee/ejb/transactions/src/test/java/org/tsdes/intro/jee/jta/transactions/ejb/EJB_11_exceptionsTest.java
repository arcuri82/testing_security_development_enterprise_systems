package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.*;


public class EJB_11_exceptionsTest extends TestBase{

    @EJB
    private EJB_11_exceptions ejb;

    @Test
    public void testAddAndThrowRuntimeException() throws Exception {

        String name = "Bar";

        assertFalse(queriesEJB.isInDB(name));

        try {
            ejb.addAndThrowRuntimeException(name);
            fail();
        } catch (RuntimeException e){
        }

        //rollback
        assertFalse(queriesEJB.isInDB(name));
    }

    @Test
    public void testAddAndThrowException() throws Exception {
        String name = "Bar";

        assertFalse(queriesEJB.isInDB(name));

        try {
            ejb.addAndThrowException(name);
            fail();
        } catch (Exception e){
        }

        // no rollback
        assertTrue(queriesEJB.isInDB(name));
    }

    @Test
    public void testAddAndThrowRuntimeExceptionNoRollback() throws Exception {

        String name = "Bar";

        assertFalse(queriesEJB.isInDB(name));

        try {
            ejb.addAndThrowRuntimeExceptionNoRollback(name);
            fail();
        } catch (RuntimeException e){
        }

        // no rollback
        assertTrue(queriesEJB.isInDB(name));
    }

    @Test
    public void testAddAndThrowExceptionWithRollback() throws Exception {

        String name = "Bar";

        assertFalse(queriesEJB.isInDB(name));

        try {
            ejb.addAndThrowExceptionWithRollback(name);
            fail();
        } catch (Exception e){
        }

        // rollback
        assertFalse(queriesEJB.isInDB(name));
    }
}