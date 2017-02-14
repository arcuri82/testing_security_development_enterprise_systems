package org.tsdes.jee.ejb.lazy;

import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by arcuri82 on 14-Feb-17.
 */
@Stateless
public class Ejb {

    @PersistenceContext
    private EntityManager em;


    public Long create(int n){

        A a = new A();
        for(int i=0; i<n; i++){
            a.getList().add(new B());
        }

        em.persist(a);

        return a.getId();
    }

    public A getLazy(long id){

        return em.find(A.class, id);
    }


    public A getInitialized(long id){

        A a = em.find(A.class, id);
        if(a == null){
            return null;
        }

        /*
            force the fetching of the list by calling one of its methods.

            However, note that this approach is not very efficient, as it might create
            one or more SQL queries.
            Another approach is to use a specific JPQL query instead of "em.find", as done
            in "jee/jpa/fetch", which will be translated in one single SQL query.
            However, it all depends on how the JPA provider (eg Hibernate or EclipseLink)
            does the conversion to SQL.
            When performance is critical, you need to double-check all the created SQL
            queries.
          */
        a.getList().size();

        return a;
    }

    public A getWrongInitialized(long id){

        A a = em.find(A.class, id);
        if(a == null){
            return null;
        }

        //this is not enough
        a.getList();

        return a;
    }

    public A getInitializedWithHibernate(long id){

        A a = em.find(A.class, id);
        if(a == null){
            return null;
        }

        //If you are using Hibernate, you can also use the following
        Hibernate.initialize(a.getList());

        return a;
    }
}
