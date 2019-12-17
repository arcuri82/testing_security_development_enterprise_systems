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

        //will fail, as a transaction will be created
        try {
            ejb.getFromRequired();
            fail();
        }catch (EJBException e){
            //expected
        }
    }

    @Test
    public void testGetFromNotSupported() throws Exception {

        assertTrue(ejb.getFromNotSupported());// no transaction, so OK
    }


    @Test
    public void testGetFromRequiredBySuspendingFirst() throws Exception {

        //OK, even if it creates a transaction, because it gets suspended
        //before calling NEVER
        assertTrue(ejb.getFromRequiredBySuspendingFirst());
    }

}