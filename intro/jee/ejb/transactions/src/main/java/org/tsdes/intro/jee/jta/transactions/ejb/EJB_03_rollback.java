package org.tsdes.intro.jee.jta.transactions.ejb;

import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class EJB_03_rollback {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;


    public void createTwo(boolean rollback, String first, String second) {

        Foo foo = new Foo(first);
        Foo bar = new Foo(second);

        em.persist(foo);
        em.persist(bar);

        if(rollback) {
            ctx.setRollbackOnly(); //none should get persisted, as we rollback
        }
    }
}
