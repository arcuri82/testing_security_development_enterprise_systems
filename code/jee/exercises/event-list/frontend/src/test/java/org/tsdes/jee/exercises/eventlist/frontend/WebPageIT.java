package org.tsdes.jee.exercises.eventlist.frontend;

import org.junit.Before;
import org.junit.Test;
import org.tsdes.jee.exercises.eventlist.frontend.po.CreateEventPageObject;
import org.tsdes.jee.exercises.eventlist.frontend.po.CreateUserPageObject;
import org.tsdes.jee.exercises.eventlist.frontend.po.HomePageObject;
import org.tsdes.jee.exercises.eventlist.frontend.po.LoginPageObject;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class WebPageIT extends WebTestBase{


    @Before
    public void startFromInitialPage() {

        assumeTrue(JBossUtil.isJBossUpAndRunning());

        home = new HomePageObject(getDriver());
        home.toStartingPage();
        home.logout();
        assertTrue(home.isOnPage());
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testHomePage(){
        home.toStartingPage();
        assertTrue(home.isOnPage());
    }


    @Test
    public void testLoginLink(){
        LoginPageObject login = home.toLogin();
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        LoginPageObject login = home.toLogin();
        HomePageObject home = login.clickLogin(getUniqueId(),"foo");
        assertNull(home);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLogin(){
        String userId = getUniqueId();
        createAndLogNewUser(userId, "Joe", "Black", "United States");
        home.logout();

        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId, "foo");

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userId));
    }

    @Test
    public void testCreateValidUser(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();
        assertTrue(create.isOnPage());

        String userName = getUniqueId();

        HomePageObject home = create.createUser(userName,"foo","foo","Foo","","Bar","Norway");
        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userName));

        home.logout();
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testCreateUserFailDueToPasswordMismatch(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();

        HomePageObject home = create.createUser(getUniqueId(),"foo","differentPassword","Foo","","Bar","Norway");
        assertNull(home);
        assertTrue(create.isOnPage());
    }


    @Test
    public void testCreateOneEvent(){

        String userId = getUniqueId();
        createAndLogNewUser(userId, "Foo", "Bar", "Norway");
        home.setShowOnlyOwnCountry(true);

        int n = home.getNumberOfDisplayedEvents();
        String title = getUniqueTitle();

        CreateEventPageObject create = home.toCreateEvent();
        home = create.createEvent(title,"Norway","Oslo","a concert");

        assertNotNull(home);
        int x = home.getNumberOfDisplayedEvents();
        assertEquals(n+1, x);
        assertTrue(getPageSource().contains(title));

        int attendees = home.getNumberOfAttendees(title);
        assertEquals(0, attendees);
    }

    @Test
    public void testCreateEventInDifferentCountries(){
        String userId = getUniqueId();
        createAndLogNewUser(userId, "Foo", "Bar", "Norway");

        home.setShowOnlyOwnCountry(false);
        int all = home.getNumberOfDisplayedEvents();

        home.setShowOnlyOwnCountry(true);
        int nor = home.getNumberOfDisplayedEvents();

        String a = "In Norway";
        String b = "In Germany";

        CreateEventPageObject create = home.toCreateEvent();
        home = create.createEvent(a,"Norway","Oslo","a concert");
        create = home.toCreateEvent();
        home = create.createEvent(b,"Germany","Berlin","another concert");

        home.setShowOnlyOwnCountry(false);

        assertEquals(all+2, home.getNumberOfDisplayedEvents());
        assertTrue(getPageSource().contains(a));
        assertTrue(getPageSource().contains(b));

        home.setShowOnlyOwnCountry(true);

        assertEquals(nor+1, home.getNumberOfDisplayedEvents());
        assertTrue(getPageSource().contains(a));
        assertFalse(getPageSource().contains(b));

        home.setShowOnlyOwnCountry(false);

        assertEquals(all+2, home.getNumberOfDisplayedEvents());
        assertTrue(getPageSource().contains(a));
        assertTrue(getPageSource().contains(b));
    }


    @Test
    public void testCreateEventsFromDifferenUsers(){

        int n = home.getNumberOfDisplayedEvents();

        String first = getUniqueId();
        createAndLogNewUser(first, "Foo", "Bar", "Norway");
        CreateEventPageObject create = home.toCreateEvent();
        home = create.createEvent(first,"Norway","Oslo","a concert");
        home.logout();

        String second = getUniqueId();
        createAndLogNewUser(second, "Foo", "Bar", "Norway");
        create = home.toCreateEvent();
        home = create.createEvent(second,"Norway","Oslo","a concert");

        home.setShowOnlyOwnCountry(false);

        assertEquals(n+2, home.getNumberOfDisplayedEvents());
        assertTrue(getPageSource().contains(first));
        assertTrue(getPageSource().contains(second));
    }


    @Test
    public void testCreateAndAttendEvent(){
        String eventName = getUniqueTitle();
        String first = getUniqueId();
        createAndLogNewUser(first, "Foo", "Bar", "Norway");
        CreateEventPageObject create = home.toCreateEvent();
        home = create.createEvent(eventName,"Norway","Oslo","a concert");

        home.setAttendance(eventName, false);
        assertFalse(home.isAttending(eventName));
        assertEquals(0, home.getNumberOfAttendees(eventName));

        home.setAttendance(eventName, true);
        assertTrue(home.isAttending(eventName));
        assertEquals(1, home.getNumberOfAttendees(eventName));

        home.setAttendance(eventName, false);
        assertFalse(home.isAttending(eventName));
        assertEquals(0, home.getNumberOfAttendees(eventName));
    }

    @Test
    public void testTwoUsersAttendingSameEvent(){

        String country = "Norway";
        String eventName = getUniqueTitle();
        String first = getUniqueId();
        createAndLogNewUser(first, "Foo", "Bar", country);
        CreateEventPageObject create = home.toCreateEvent();
        home = create.createEvent(eventName,country,"Oslo","a concert");

        home.setAttendance(eventName, true);
        home.logout();

        String second = getUniqueId();
        createAndLogNewUser(second, "Foo", "Bar", country);
        home.setAttendance(eventName, true);

        assertEquals(2, home.getNumberOfAttendees(eventName));
        home.logout();

        String third = getUniqueId();
        createAndLogNewUser(third, "Foo", "Bar", country);

        assertEquals(2, home.getNumberOfAttendees(eventName));
        assertFalse(home.isAttending(eventName));
        home.logout();

        loginExistingUser(first);
        assertEquals(2, home.getNumberOfAttendees(eventName));
        assertTrue(home.isAttending(eventName));

        home.setAttendance(eventName, false);
        assertEquals(1, home.getNumberOfAttendees(eventName));
        assertFalse(home.isAttending(eventName));

    }
}
