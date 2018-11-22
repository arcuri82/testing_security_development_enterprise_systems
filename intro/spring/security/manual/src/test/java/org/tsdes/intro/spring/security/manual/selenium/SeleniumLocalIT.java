package org.tsdes.intro.spring.security.manual.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.security.manual.selenium.po.IndexPO;
import org.tsdes.intro.spring.security.manual.selenium.po.LoginPO;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {

    private static WebDriver driver;

    @LocalServerPort
    private int port;

    private IndexPO home;

    private static final AtomicInteger counter = new AtomicInteger(0);

    private String getUniqueId(){
        return "foo_SeleniumLocalIT_" + counter.getAndIncrement();
    }


    @BeforeAll
    public static void initClass() {

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver != null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    @BeforeEach
    public void initTest() {

        /*
            we want to have a new session, otherwise the tests
            will share the same Session beans
         */
        driver.manage().deleteAllCookies();

        home = new IndexPO(driver, "localhost", port);

        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from Home Page");
    }


    @Test
    public void testHomePage() {
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedOut());
    }

    @Test
    public void testCreateUser() {
        LoginPO login = home.doLogin();
        assertTrue(login.isOnPage());
        assertFalse(home.isOnPage());

        String userId = getUniqueId();
        String password = "foo";

        home = login.createUser(userId, password);
        assertFalse(login.isOnPage());
        assertTrue(home.isOnPage());

        assertTrue(home.getDriver().getPageSource().contains(userId));
        assertTrue(home.isLoggedIn());
    }


    @Test
    public void testCreatePost() {

        String text = "Some post at " + System.currentTimeMillis();

        assertTrue(home.isLoggedOut());
        boolean created = home.createPost(text);
        //shouldn't be able to create post when logged out
        assertFalse(created);

        int n = home.getNumberOfPosts();
        int d = home.getNumberOfPostsThatCanBeDeleted();
        assertEquals(0, d); // cannot delete anything when logged out

        String userId = getUniqueId();
        LoginPO login = home.doLogin();
        home = login.createUser(userId, "foo");

        created = home.createPost(text);
        assertTrue(created);
        assertEquals(n+1, home.getNumberOfPosts());
        assertEquals(1, home.getNumberOfPostsThatCanBeDeleted());
        assertTrue(home.getDriver().getPageSource().contains(text));

        //now logout: post should still be there, but not deletable
        home.doLogout();
        assertEquals(n+1, home.getNumberOfPosts());
        assertEquals(0, home.getNumberOfPostsThatCanBeDeleted());
    }
}
