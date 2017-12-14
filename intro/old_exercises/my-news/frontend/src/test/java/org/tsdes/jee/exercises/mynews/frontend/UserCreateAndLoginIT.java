package org.tsdes.jee.exercises.mynews.frontend;

import org.junit.Test;
import org.tsdes.jee.exercises.mynews.frontend.po.CreateUserPageObject;
import org.tsdes.jee.exercises.mynews.frontend.po.HomePageObject;
import org.tsdes.jee.exercises.mynews.frontend.po.LoginPageObject;


import static org.junit.Assert.*;

public class UserCreateAndLoginIT extends WebTestBase{


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
        createAndLogNewUser(userId, "Joe", "Black");
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

        HomePageObject home = create.createUser(userName,"foo","foo","Foo","","Bar");
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

        HomePageObject home = create.createUser(getUniqueId(),"foo","differentPassword","Foo","","Bar");
        assertNull(home);
        assertTrue(create.isOnPage());
    }


}
