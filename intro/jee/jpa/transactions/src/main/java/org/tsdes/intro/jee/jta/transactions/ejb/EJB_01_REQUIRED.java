package org.tsdes.intro.jee.jta.transactions.ejb;


import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED) //this is the default
public class EJB_01_REQUIRED {

    @PersistenceContext
    private EntityManager em;

    public void createFoo(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }
}
