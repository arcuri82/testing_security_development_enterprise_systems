package org.tsdes.intro.jee.jta.transactions.ejb;


import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
/*
    REQUIRED is the default, so it is technically unnecessary here.
    Notice that applying this annotation on the class will be equivalent
    to apply it on all its public methods.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EJB_01_REQUIRED {

    @PersistenceContext
    private EntityManager em;

    public void createFoo(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }
}
