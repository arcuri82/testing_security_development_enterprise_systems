package org.tsdes.jee.jta.transactions.ejb;

import org.tsdes.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_07_MANDATORY {

    @PersistenceContext
    private EntityManager em;

    public void createFooRequired(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void createFooMandatory(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public boolean isPresentMandatory(String name){
        return em.find(Foo.class, name) != null;
    }


    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void createFooSupports(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isPresentSupports(String name){
        return em.find(Foo.class, name) != null;
    }

}
