package org.tsdes.jee.exercises.mycantina.backend.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.exercises.mycantina.backend.entity.Menu;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 22-May-17.
 */
@RunWith(Arquillian.class)
public class MenuEJBTest extends EjbTestBase {

    @Test
    public void testCreateMenuWithNoDish() {

        try {
            menuEJB.createMenu(LocalDate.now(), null);
            fail();
        } catch (Exception e) {
            //expected
        }
    }


    @Test
    public void testGetCurrentMenu() {

        long dishId = dishEJB.createDish("foo", "bar");

        long menuId = menuEJB.createMenu(LocalDate.now(), Arrays.asList(dishId));

        Menu current = menuEJB.getCurrent();
        assertEquals(1, current.getDishes().size());
        assertEquals(menuId, (long) current.getId());
    }

    @Test
    public void testGetAbsentPreviousMenu() {

        long dishId = dishEJB.createDish("foo", "bar");
        menuEJB.createMenu(LocalDate.now(), Arrays.asList(dishId));

        Menu previous = menuEJB.getPrevious(LocalDate.now());
        assertNull(previous);
    }

    @Test
    public void testGetAbsentNextMenu() {

        long dishId = dishEJB.createDish("foo", "bar");
        menuEJB.createMenu(LocalDate.now(), Arrays.asList(dishId));

        Menu previous = menuEJB.getNext(LocalDate.now());
        assertNull(previous);
    }

    @Test
    public void testGetPreviousMenu() {

        long dishId = dishEJB.createDish("foo", "bar");
        menuEJB.createMenu(LocalDate.now(), Arrays.asList(dishId));
        menuEJB.createMenu(LocalDate.now().minusDays(1), Arrays.asList(dishId));

        Menu previous = menuEJB.getPrevious(LocalDate.now());
        assertNotNull(previous);
    }

    @Test
    public void testGetNextMenu() {

        long dishId = dishEJB.createDish("foo", "bar");
        menuEJB.createMenu(LocalDate.now(), Arrays.asList(dishId));
        menuEJB.createMenu(LocalDate.now().plusDays(1), Arrays.asList(dishId));

        Menu next = menuEJB.getNext(LocalDate.now());
        assertNotNull(next);
    }

    @Test
    public void testThreeMenus() {

        long dishId = dishEJB.createDish("foo", "bar");
        long beforeId = menuEJB.createMenu(LocalDate.now().minusDays(1), Arrays.asList(dishId));
        long nowId = menuEJB.createMenu(LocalDate.now(), Arrays.asList(dishId));
        long afterId = menuEJB.createMenu(LocalDate.now().plusDays(1), Arrays.asList(dishId));

        Menu current = menuEJB.getCurrent();
        assertEquals(nowId, (long) current.getId());
        assertEquals(beforeId, (long) menuEJB.getPrevious(current.getDate()).getId());
        assertEquals(afterId, (long) menuEJB.getNext(current.getDate()).getId());

        Menu next = menuEJB.getNext(current.getDate());
        assertEquals(afterId, (long) next.getId());
        assertEquals(nowId, (long) menuEJB.getPrevious(next.getDate()).getId());
        assertNull(menuEJB.getNext(next.getDate()));

        Menu previous = menuEJB.getPrevious(current.getDate());
        assertEquals(beforeId, (long) previous.getId());
        assertEquals(nowId, (long) menuEJB.getNext(previous.getDate()).getId());
        assertNull(menuEJB.getPrevious(previous.getDate()));
    }
}
