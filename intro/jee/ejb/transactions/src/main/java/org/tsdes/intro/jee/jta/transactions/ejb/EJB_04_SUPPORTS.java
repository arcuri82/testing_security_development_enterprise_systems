package org.tsdes.intro.jee.jta.transactions.ejb;


import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_04_SUPPORTS {

    @PersistenceContext
    private EntityManager em;


    //Redundant annotation, as REQUIRED is the default
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createFooWithRequiredTransaction(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }


    /*
        if a transaction is not needed, for performance one can instruct
        the container to do not create one (which would had been the default
        behavior in a REQUIRED).
        However, if we are currently in a transaction, join it.
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isPresentWithSupports(String name){
        return em.find(Foo.class, name) != null;
    }


    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void createFooWithSupports(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }



    public void createTwo(String first, String second){
        //here we are in a transaction

        createFooWithRequiredTransaction(first); //in same transaction, no need to create a new one

        /*
            as we are in a transaction, this will work.
            however, this is regardless of the annotation,
            as this is a java call from inside the EJB, and not
            the proxy created by the JEE container.
            we ll see this point in more details later
         */
        createFooWithSupports(second);
    }
}
