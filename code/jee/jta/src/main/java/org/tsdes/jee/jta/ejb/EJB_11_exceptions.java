package org.tsdes.jee.jta.ejb;


import org.tsdes.jee.jta.data.Foo;

import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_11_exceptions {

    @PersistenceContext
    private EntityManager em;


    public void addAndThrowRuntimeException(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
        throw new RuntimeException("Transaction should roll back");
    }


    public void addAndThrowException(String name)
            throws Exception //NOTE: need this one, otherwise it will not compile
    {
        Foo foo = new Foo(name);
        em.persist(foo);
        throw new Exception("Checked exceptions will not lead to rollback");
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
