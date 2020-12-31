package org.tsdes.intro.jee.jta.transactions.ejb;


import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_11_exceptions {

    /*
        The distinction between CHECKED and UNCHECKED exceptions in Java is a relic
        of the past.
        CHECKED exceptions must always be handled (either with "throws" on method signature,
        or with try/catch).
        But they only exist at compilation time, and NOT in the JVM, ie not at runtime.
        This is the reason why modern languages running on the JVM like Kotlin do not deal
        with checked exceptions (even when using Java APIs).
     */

    @PersistenceContext
    private EntityManager em;


    public void addAndThrowRuntimeException(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
        /*
            RuntimeException (and any other exception subclassing it) is UNCHECKED
         */
        throw new RuntimeException("Transaction should roll back");
    }


    public void addAndThrowException(String name)
            throws Exception //NOTE: need this one, otherwise it will not compile
    {
        Foo foo = new Foo(name);
        em.persist(foo);
        //this is a CHECKED exception
        throw new Exception("Checked exceptions will not lead to rollback");
        /*
            unfortunately JPA does NOT rollback transactions in the case of checked exceptions.
            It does not really make any sense... this yet another case for loathing
            checked exceptions
         */
    }

    public void addAndThrowRuntimeExceptionNoRollback(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
        throw new NoRollbackRuntimeException("Should not roll back");
    }


    public void addAndThrowExceptionWithRollback(String name)
            throws Exception //NOTE: need this one, otherwise it will not compile
    {
        Foo foo = new Foo(name);
        em.persist(foo);
        throw new WithRollbackException("Rollback");
    }

    /*
        JPA treats checked and unchecked exceptions differently when it comes to rollbacks.
        but this behavior can be modified with annotations.
     */

    @ApplicationException(rollback = false)
    private static class NoRollbackRuntimeException extends RuntimeException{
        public NoRollbackRuntimeException(String message) {
            super(message);
        }
    }

    @ApplicationException(rollback = true)
    private static class WithRollbackException extends Exception{
        public WithRollbackException(String message) {
            super(message);
        }
    }
}
