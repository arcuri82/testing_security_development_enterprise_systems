package org.tsdes.intro.jee.jta.transactions.ejb;

import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_02_abort {

    @PersistenceContext
    private EntityManager em;

    public void createTwoCopies(String name){

        Foo foo = new Foo(name);
        Foo copy = new Foo(name);

        em.persist(foo);
        em.persist(copy);//should fail, because same id

        /*
                The exception is thrown when em.persist(copy) is executed.
                However, different JPA providers might behave differently,
                and throw only once this method is completed,
                ie, when the JEE container will execute the transaction toward
                the database. Recall, 'em' here is just a cache.
         */
    }
}
