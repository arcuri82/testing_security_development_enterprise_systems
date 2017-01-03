package org.tsdes.jee.exercises.eventlist.backend.util;


import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*
    Utility EJB to use ONLY in an Arquillian deployment to clean up the database.
    Note: you want this EJB under src/test/java, and NEVER under src/main/java.
    Plus, should really make sure that you are not running tests against a live,
    production database...
 */
@Stateless
public class DeleterEJB {

    @PersistenceContext
    private EntityManager em;

    public void deleteEntities(Class<?> entity){

        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        /*
            Note: we passed as input a Class<?> instead of a String to
            avoid SQL injection. However, being here just test code, it should
            not be a problem. But, as a good habit, always be paranoiac about
            security, above all when you have code that can delete the whole
            database...
         */

        Query query = em.createQuery("delete from " + name);
        query.executeUpdate();
    }

}
