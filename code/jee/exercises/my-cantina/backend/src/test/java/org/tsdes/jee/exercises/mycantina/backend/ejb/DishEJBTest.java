package org.tsdes.jee.exercises.mycantina.backend.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 22-May-17.
 */
@RunWith(Arquillian.class)
public class DishEJBTest extends EjbTestBase{

    @Test
    public void testCreateDish(){

        assertEquals(0, dishEJB.getAllDishes().size());

        dishEJB.createDish("foo", "bar");

        assertEquals(1, dishEJB.getAllDishes().size());
    }

    @Test
    public void testCreateTwoDishes(){
        assertEquals(0, dishEJB.getAllDishes().size());

        dishEJB.createDish("foo", "bar");
        dishEJB.createDish("foo 2", "bar 2");

        assertEquals(2, dishEJB.getAllDishes().size());
    }

    @Test
    public void testCanDelete(){

        long first = dishEJB.createDish("foo", "bar");
        long second = dishEJB.createDish("a", "b");

        assertTrue(dishEJB.canBeDeleted(first));

        menuEJB.createMenu(LocalDate.now(), Arrays.asList(second));

        //can still delete
        assertTrue(dishEJB.canBeDeleted(first));

        menuEJB.createMenu(LocalDate.now().plusDays(1), Arrays.asList(first));

        assertFalse(dishEJB.canBeDeleted(first));
    }

    @Test
    public void testDelete(){

        long dish = dishEJB.createDish("foo", "bar");
        assertNotNull(dishEJB.getDish(dish));
        assertTrue(dishEJB.canBeDeleted(dish));

        dishEJB.delete(dish);

        assertNull(dishEJB.getDish(dish));
        assertFalse(dishEJB.canBeDeleted(dish));
    }
}