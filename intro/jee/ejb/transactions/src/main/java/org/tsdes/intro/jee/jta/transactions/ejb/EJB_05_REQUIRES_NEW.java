package org.tsdes.intro.jee.jta.transactions.ejb;


import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_05_REQUIRES_NEW {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;


    public void createFooRequired(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }

    /*
        This create a new transaction. If one is currently activated,
        put it on hold first and do not join it
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createFooRequiresNew(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }


    public void createTwoWithRollback(String first, String second){

        //call from inside instance are not on the proxy, so annotations ignored
        createFooRequired(first); //would had used current transaction
        createFooRequiresNew(second); //would had created a new one

        /*
            would expect no impact on "second", but it does, because those above
            are not EJB calls, but direct Java calls from within the EJB,
            and not on the proxy that does handle the transactions
         */
        ctx.setRollbackOnly();
    }

    public void createTwoWithRollbackInEJBCall(String first, String second){

        createFooRequired(first); //uses current transaction

        //this is a new proxy, so calls on it will have the annotations applied
        EJB_05_REQUIRES_NEW ejb = ctx.getBusinessObject(EJB_05_REQUIRES_NEW.class);
        ejb.createFooRequiresNew(second); //creates a new transactions
        //after method is completed (and its transaction completed), the previous
        //transactions is resumed

        ctx.setRollbackOnly(); //should have no impact on "second", as already persisted
    }

    public void createTwoWithRollbackInEJBCallOnSameTransaction(String first, String second){

        createFooRequired(first); //uses current transaction

        EJB_05_REQUIRES_NEW ejb = ctx.getBusinessObject(EJB_05_REQUIRES_NEW.class);
        //same transaction, even if EJB call, as REQUIRED will NOT create a new
        //transaction if there is an ongoing one. it will just join it
        ejb.createFooRequired(second);

        ctx.setRollbackOnly(); //should have impact on both
    }



}
