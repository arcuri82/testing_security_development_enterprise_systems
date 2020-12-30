package org.tsdes.intro.spring.security.framework.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.security.framework.Application;
import org.tsdes.intro.spring.security.framework.selenium.po.IndexPO;
import org.tsdes.intro.spring.security.framework.selenium.po.LoginPO;
import org.tsdes.intro.spring.security.framework.selenium.po.ProtectedPO;
import org.tsdes.intro.spring.security.framework.selenium.po.SignupPO;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
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
        SignupPO signup = home.doSignup();
        assertTrue(signup.isOnPage());
        assertFalse(home.isOnPage());

        String userId = getUniqueId();
        String password = "foo";

        home = signup.createUser(userId, password);
        assertFalse(signup.isOnPage());
        assertTrue(home.isOnPage());

        assertTrue(home.getDriver().getPageSource().contains(userId));
        assertTrue(home.isLoggedIn());
    }

    @Test
    public void testProtectedResource(){

        assertTrue(home.isLoggedOut());

        ProtectedPO protectedPO = home.goToProtectedPage();
        assertNull(protectedPO);

        SignupPO signup = home.doSignup();
        home = signup.createUser(getUniqueId(), "foo");

        assertTrue(home.isLoggedIn());

        protectedPO = home.goToProtectedPage();
        assertTrue(protectedPO.isOnPage());

        home = protectedPO.doLogout();
        assertTrue(home.isLoggedOut());
    }


    @Test
    public void testSignUpLogOutLogIn(){

        assertTrue(home.isLoggedOut());

        SignupPO signup = home.doSignup();
        String userId = getUniqueId();
        String password = "foo";
        home = signup.createUser(userId, password);

        assertTrue(home.isLoggedIn());

        home.doLogout();

        assertTrue(home.isLoggedOut());

        LoginPO login = home.doLogin();
        home = login.loginUser(userId, password);

        assertTrue(home.isLoggedIn());
    }
}