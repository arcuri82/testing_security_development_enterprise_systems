package org.tsdes.intro.jee.jta.transactions.ejb;

import org.junit.Test;

import javax.transaction.Status;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 13-Feb-18.
 */
public class EJB_12_statusTest extends TestBase{


    @Test
    public void testRequired(){

        EJB_12_status ejb = getEJB(EJB_12_status.class);

        int status  = ejb.getInRequired();

        assertEquals(Status.STATUS_ACTIVE, status);
    }

    @Test
    public void testNotSupported(){

        EJB_12_status ejb = getEJB(EJB_12_status.class);

        int status  = ejb.getInNotSupported();

        assertEquals(Status.STATUS_NO_TRANSACTION, status);
    }

    @Test
    public void testPostConstruct(){

        EJB_12_status ejb = getEJB(EJB_12_status.class);

        int status  = ejb.getTransactionStatusInPostConstruct();

        assertEquals(Status.STATUS_ACTIVE, status);
    }


}