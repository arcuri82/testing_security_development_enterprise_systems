package org.tsdes.intro.jee.jta.transactions.ejb;

import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

/**
 * Created by arcuri82 on 13-Feb-18.
 */
@Singleton
public class EJB_13_selfinjection {


    @PersistenceContext
    private EntityManager em;

    @EJB
    private EJB_13_selfinjection proxy;

    public boolean createFailDirect() {

        String name = "bar";
        Foo foo = new Foo(name);
        Foo copy = new Foo(name);

        em.persist(foo);
        em.persist(copy);

        return true;
    }

    @TransactionAttribute(NOT_SUPPORTED)
    public boolean createFailIndirect() {

        try {
            proxy.createFailDirect();
        } catch (EJBException e) {
            return false;
        }

        return true;
    }
}
