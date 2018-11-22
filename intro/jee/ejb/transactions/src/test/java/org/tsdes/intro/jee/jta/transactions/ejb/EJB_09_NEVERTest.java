package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.jupiter.api.Test;

import javax.ejb.EJBException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class EJB_09_NEVERTest extends TestBase{

    @Test
    public void testGetTrue() throws Exception {
        EJB_09_NEVER ejb = getEJB(EJB_09_NEVER.class);

        assertTrue(ejb.getTrue()); //fine, as we are not in a transaction
    }

    @Test
    public void testGetFromRequired() throws Exception {
        EJB_09_NEVER ejb = getEJB(EJB_09_NEVER.class);

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