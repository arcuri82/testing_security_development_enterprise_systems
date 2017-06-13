package org.tsdes.jee.exercises.mycantina.backend.ejb;

import org.tsdes.jee.exercises.mycantina.backend.entity.Dish;
import org.tsdes.jee.exercises.mycantina.backend.entity.Menu;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by arcuri82 on 22-May-17.
 */
@Stateless
public class MenuEJB {

    @PersistenceContext
    private EntityManager em;

    public Long createMenu(@NotNull LocalDate date, List<Long> dishIds) {

        Menu menu;

        Query query = em.createQuery("select m from Menu m where m.date = :date");
        query.setParameter("date", date);
        List list = query.getResultList();
        if(list.isEmpty()){
            menu = new Menu();
            menu.setDate(date);
        } else {
            menu = (Menu) list.get(0);
        }

        menu.getDishes().clear();

        if (dishIds != null) {
            for (Long id : dishIds) {
                Dish dish = em.find(Dish.class, id);
                if (dish == null) {
                    throw new IllegalArgumentException("Invalid id for Dish");
                }
                menu.getDishes().add(dish);
            }
        }

        if(menu.getId() == null) {
            em.persist(menu);
        }

        return menu.getId();
    }

    public Menu getCurrent() {

        Query query = em.createQuery("select m from Menu m where (m.date >= current_date) order by m.date");
        query.setMaxResults(1);
        List list = query.getResultList();
        if (!list.isEmpty()) {
            return (Menu) list.get(0);
        }
        return getPrevious(LocalDate.now());
    }

    public Menu getNext(@NotNull LocalDate current) {
        Query query = em.createQuery("select m from Menu m where (m.date > :current) order by m.date ASC ");
        query.setParameter("current", current);
        query.setMaxResults(1);

        List list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        } else {
            return (Menu) list.get(0);
        }
    }

    public Menu getPrevious(@NotNull LocalDate current) {
        Query query = em.createQuery("select m from Menu m where (m.date < :current) order by m.date DESC ");
        query.setParameter("current", current);
        query.setMaxResults(1);

        List list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        } else {
            return (Menu) list.get(0);
        }
    }
}
