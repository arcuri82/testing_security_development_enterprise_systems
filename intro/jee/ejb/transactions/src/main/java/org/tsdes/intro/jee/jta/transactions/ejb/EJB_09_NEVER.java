package org.tsdes.intro.jee.jta.transactions.ejb;


import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_09_NEVER {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;


    @TransactionAttribute(TransactionAttributeType.NEVER)
    public boolean getTrue(){
        return true;
    }


    public boolean getFromRequired(){
        EJB_09_NEVER ejb = ctx.getBusinessObject(EJB_09_NEVER.class);
        return ejb.getTrue(); //will fail if transaction was created
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean getFromNotSupported(){
        EJB_09_NEVER ejb = ctx.getBusinessObject(EJB_09_NEVER.class);
        return ejb.getTrue(); //will not fail, as the transaction (if any) was suspended
    }


    public boolean getFromRequiredBySuspendingFirst(){
        EJB_09_NEVER ejb = ctx.getBusinessObject(EJB_09_NEVER.class);
        return ejb.getFromNotSupported(); //will suspend the transaction
    }

}
