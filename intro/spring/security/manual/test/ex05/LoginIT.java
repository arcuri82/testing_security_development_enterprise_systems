package org.tsdes.intro.jee.jsf.examples.ex05;

import org.junit.Before;
import org.junit.Test;
import org.tsdes.intro.jee.jsf.examples.ex05.po.HomePageObject;
import org.tsdes.intro.jee.jsf.examples.ex05.po.LoginPageObject;
import org.tsdes.intro.jee.jsf.examples.test.SeleniumTestBase;

import static org.junit.Assert.*;

public class LoginIT extends SeleniumTestBase {

    private HomePageObject po;

    @Before
    public void toStartingPage() {
        po = new HomePageObject(getDriver());
        po.toStartingPage();
    }


    @Test
    public void testHomePage() {
        assertTrue(po.isOnPage());
        assertTrue(po.isLoggedOut());
    }

    @Test
    public void testCreateUser() {
        LoginPageObject login = po.doLogin();
        assertTrue(login.isOnPage());
        assertFalse(po.isOnPage());

        String userId = "testCreateUser_" + System.currentTimeMillis();
        String password = "foo";

        po = login.createUser(userId, password);
        assertFalse(login.isOnPage());
        assertTrue(po.isOnPage());

        assertTrue(getDriver().getPageSource().contains(userId));
        assertTrue(po.isLoggedIn());
    }


    @Test
    public void testCreatePost() {

        String text = "Some post at " + System.currentTimeMillis();

        assertTrue(po.isLoggedOut());
        boolean created = po.createPost(text);
        //shouldn't be able to create post when logged out
        assertFalse(created);

        int n = po.getNumberOfPosts();
        int d = po.getNumberOfPostsThatCanBeDeleted();
        assertEquals(0, d); // cannot delete anything when logged out

        String userId = "testCreatePost_" + System.currentTimeMillis();
        LoginPageObject login = po.doLogin();
        po = login.createUser(userId, "foo");

        created = po.createPost(text);
        assertTrue(created);
        assertEquals(n+1, po.getNumberOfPosts());
        assertEquals(1, po.getNumberOfPostsThatCanBeDeleted());
        assertTrue(getDriver().getPageSource().contains(text));

        //now logout: post should still be there, but not deletable
        po.doLogout();
        assertEquals(n+1, po.getNumberOfPosts());
        assertEquals(0, po.getNumberOfPostsThatCanBeDeleted());
    }
}
