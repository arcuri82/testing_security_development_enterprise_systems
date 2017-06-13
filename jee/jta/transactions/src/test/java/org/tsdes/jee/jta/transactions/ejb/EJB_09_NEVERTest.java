package org.tsdes.jee.jta.transactions.ejb;

import org.junit.Test;

import javax.ejb.EJBException;

import static org.junit.Assert.*;

public class EJB_09_NEVERTest extends TestBase{

    @Test
    public void testGetTrue() throws Exception {
        EJB_09_NEVER ejb = getEJB(EJB_09_NEVER.class);

        assertTrue(ejb.getTrue()); //fine, as we are not in a transaction
    }

    @Test(expected = EJBException.class)
    public void testGetFromRequired() throws Exception {
        EJB_09_NEVER ejb = getEJB(EJB_09_NEVER.class);

        ejb.getFromRequired();//will fail, as a transaction will be created
    }

    @Test
    public void testGetFromNotSupported() throws Exception {
        EJB_09_NEVER ejb = getEJB(EJB_09_NEVER.class);

        assertTrue(ejb.getFromNotSupported());// no transaction, so OK
    }


    @Test
    public void testGetFromRequiredBySuspendingFirst() throws Exception {

        EJB_09_NEVER ejb = getEJB(EJB_09_NEVER.class);

        //OK, even if it creates a transaction, because it gets suspended
        //before calling NEVER
        assertTrue(ejb.getFromRequiredBySuspendingFirst());
    }

}