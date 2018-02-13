package org.tsdes.intro.jee.jta.transactions.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.transaction.TransactionSynchronizationRegistry;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

/**
 * Created by arcuri82 on 13-Feb-18.
 */
@Singleton
public class EJB_12_status {

    @Resource
    private TransactionSynchronizationRegistry tsr;

    /*
        why -1? just making sure these are set, and not left on 0,
        which means STATUS_ACTIVE
     */

    private int transactionStatusInPostConstruct = -1;


    @PostConstruct
    public void init(){
        transactionStatusInPostConstruct = tsr.getTransactionStatus();
    }

    public int getInRequired(){
        return tsr.getTransactionStatus();
    }

    @TransactionAttribute(NOT_SUPPORTED)
    public int getInNotSupported(){
        return tsr.getTransactionStatus();
    }


    public int getTransactionStatusInPostConstruct() {
        return transactionStatusInPostConstruct;
    }

}
