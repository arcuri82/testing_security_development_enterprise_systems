package org.tsdes.jee.jta.transactions.ejb;

import org.tsdes.jee.jta.transactions.data.Foo;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_08_NOT_SUPPORTED {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void createFooNotSupported(String name){
        Foo foo = new Foo(name);
        em.persist(foo); //will fail out of a transaction
        // NOTE: only as example... usually would not have a "persist" in
        // a NOT_SUPPORTED method
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void createFooSupports(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }


    public void createFooIndirectly(String name){
        //should work, as NOT_SUPPORTED is ignored
        createFooNotSupported(name);
    }


    public void createFooIndirectlyWithEJBCall(String name){
        EJB_08_NOT_SUPPORTED ejb = ctx.getBusinessObject(EJB_08_NOT_SUPPORTED.class);
        ejb.createFooNotSupported(name); //fail, because transaction get suspended
    }

    public void createFooIndirectlyWithEJBCallWithSupports(String name){
        EJB_08_NOT_SUPPORTED ejb = ctx.getBusinessObject(EJB_08_NOT_SUPPORTED.class);
        ejb.createFooSupports(name); //will not fail as we are in a transaction
    }


}
