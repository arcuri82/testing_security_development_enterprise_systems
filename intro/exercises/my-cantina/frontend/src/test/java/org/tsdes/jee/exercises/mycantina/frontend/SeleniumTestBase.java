package org.tsdes.jee.exercises.mycantina.frontend;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.tsdes.jee.exercises.mycantina.frontend.po.DishesPageObject;
import org.tsdes.jee.exercises.mycantina.frontend.po.HomePageObject;
import org.tsdes.jee.exercises.mycantina.frontend.po.MenuPageObject;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 28-Nov-17.
 */
public abstract class SeleniumTestBase {

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    protected static HomePageObject home;

    protected abstract WebDriver getDriver();

    protected abstract String getJeeHost();

    protected abstract int getJeePort();


    protected static String getUniqueId() {
        return "foo" + counter.incrementAndGet();
    }


    @Before
    public void startFromInitialPage() {

        home = new HomePageObject(getDriver(), getJeeHost(), getJeePort());
        home.toStartingPage();
        assertTrue(home.isOnPage());
    }


    @Test
    public void testHomePage() {
        assertTrue(home.isOnPage());
    }

    @Test
    public void testCreateDish() {

        DishesPageObject dishes = home.toDishes();

        String unique = getUniqueId();

        assertFalse(dishes.hasDishByName(unique));

        dishes.createDish(unique, "foo");

        assertTrue(dishes.hasDishByName(unique));
    }

    private String createUniqueDish() {

        home.toStartingPage();

        DishesPageObject dishes = home.toDishes();
        String unique = getUniqueId();
        dishes.createDish(unique, "foo");

        dishes.toHomePage();

        return unique;
    }

    @Test
    public void testMenu() {

        String a = createUniqueDish();
        String b = createUniqueDish();
        String c = createUniqueDish();

        MenuPageObject menu = home.toMenu();
        assertTrue(menu.hasDishByName(a));
        assertTrue(menu.hasDishByName(b));
        assertTrue(menu.hasDishByName(c));

        assertFalse(menu.isDishSelected(a));
        assertFalse(menu.isDishSelected(b));
        assertFalse(menu.isDishSelected(c));

        menu.selectDishByName(a, true);
        menu.selectDishByName(b, false);
        menu.selectDishByName(c, true);

        assertTrue(menu.isDishSelected(a));
        assertFalse(menu.isDishSelected(b));
        assertTrue(menu.isDishSelected(c));

        HomePageObject backHome = menu.create(LocalDate.now());
        assertNotNull(backHome);

        backHome.displayDefault();
        assertEquals(LocalDate.now(), backHome.getDisplayedDate());

        List<String> names = backHome.getDisplayedDishNames();
        assertEquals(2, names.size());
        assertTrue(names.contains(a));
        assertTrue(names.contains(c));
    }

    private void createMenu(LocalDate date, String dish) {
        MenuPageObject menu = home.toMenu();
        menu.selectDishByName(dish, true);
        menu.create(date);
    }

    @Test
    public void testDifferentDates() {

        String dish = createUniqueDish();

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate yesterday = LocalDate.now().minusDays(1);

        createMenu(today, dish);
        createMenu(tomorrow, dish);
        createMenu(yesterday, dish);
        home.displayDefault();

        assertEquals(today, home.getDisplayedDate());

        home.clickNext();
        assertEquals(tomorrow, home.getDisplayedDate());

        home.clickPrevious();
        assertEquals(today, home.getDisplayedDate());

        home.clickPrevious();
        assertEquals(yesterday, home.getDisplayedDate());
    }
}
