package org.tsdes.jee.exercises.quizgame.backend.ejb;


import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DeleterEJB {

    @PersistenceContext
    private EntityManager em;

    public void deleteEntityById(Class<?> entity, Object id){
        Object obj = em.find(entity, id);
        em.remove(obj);
    }

    public void deleteEntities(Class<?> entity){

        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        Query query = em.createQuery("delete from " + name);
        query.executeUpdate();
    }

}
