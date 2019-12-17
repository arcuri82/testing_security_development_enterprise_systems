package org.tsdes.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;
import javax.transaction.Status;

import static org.junit.Assert.assertEquals;


/**
 * Created by arcuri82 on 13-Feb-18.
 */
public class EJB_12_statusTest extends TestBase{

    @EJB
    private EJB_12_status ejb;

    @Test
    public void testRequired(){

        int status  = ejb.getInRequired();

        assertEquals(Status.STATUS_ACTIVE, status);
    }

    @Test
    public void testNotSupported(){

        int status  = ejb.getInNotSupported();

        assertEquals(Status.STATUS_NO_TRANSACTION, status);
    }

    @Test
    public void testPostConstruct(){

        int status  = ejb.getTransactionStatusInPostConstruct();

        assertEquals(Status.STATUS_ACTIVE, status);
    }


}