package org.tsdes.jee.exercises.mycantina.backend.ejb;

import org.tsdes.jee.exercises.mycantina.backend.entity.Dish;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by arcuri82 on 22-May-17.
 */
@Stateless
public class DishEJB {

    @PersistenceContext
    private EntityManager em;


    public Long createDish(String name, String description){

        Dish dish = new Dish();
        dish.setName(name);
        dish.setDescription(description);

        em.persist(dish);

        return dish.getId();
    }

    public Dish getDish(long id){
        return em.find(Dish.class, id);
    }

    public List<Dish> getAllDishes(){
        Query query = em.createQuery("select d from Dish d order by d.name");
        return query.getResultList();
    }

    public boolean canBeDeleted(long id){

        Dish dish = getDish(id);
        if(dish == null){
            return false;
        }

        Query query = em.createQuery("select m from Menu m where :dish member of m.dishes");
        query.setParameter("dish", dish);
        query.setMaxResults(1);
        List results = query.getResultList();

        return results==null || results.isEmpty();
    }

    public void delete(long id){
        Dish dish = getDish(id);
        if(dish != null){
            em.remove(dish);
        }
    }
}
