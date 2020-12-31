package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class EJB_09_NEVERTest extends TestBase{

    @EJB
    private EJB_09_NEVER ejb;

    @Test
    public void testGetTrue() throws Exception {

        assertTrue(ejb.getTrue()); //fine, as we are not in a transaction
    }

    @Test
    public void testGetFromRequired() throws Exception {

        //will fail, as a transaction will be created through REQUIRED,
        //but then we end up with a call on NEVER
        try {
            ejb.getFromRequired();
            fail();
        }catch (EJBException e){
            //expected
        }
    }

    @Test
    public void testGetFromNotSupported() throws Exception {

        // no transaction, so OK when calling NEVER
        assertTrue(ejb.getFromNotSupported());
    }


    @Test
    public void testGetFromRequiredBySuspendingFirst() throws Exception {

        /*
            OK, even if it creates a transaction, because it gets suspended
            before calling NEVER.
            It other words:
            - REQUIRED does a create a new transaction (as tests are not in transaction)
            - NOT_SUPPORTED will put the transaction on hold
            - NEVER is fine, as there is no active transaction when called
         */
        assertTrue(ejb.getFromRequiredBySuspendingFirst());
    }

}