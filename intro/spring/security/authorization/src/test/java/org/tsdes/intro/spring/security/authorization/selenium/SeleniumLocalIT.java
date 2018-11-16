package org.tsdes.intro.spring.security.authorization.selenium;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.spring.security.authorization.Application;
import org.tsdes.intro.spring.security.authorization.selenium.po.IndexPO;
import org.tsdes.intro.spring.security.authorization.selenium.po.LoginPO;
import org.tsdes.intro.spring.security.authorization.selenium.po.ProtectedPO;
import org.tsdes.intro.spring.security.authorization.selenium.po.SignupPO;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
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


    @BeforeClass
    public static void initClass() {

        driver = SeleniumDriverHandler.getChromeDriver();

        if (driver == null) {
            //Do not fail the tests.
            throw new AssumptionViolatedException("Cannot find/initialize Chrome driver");
        }
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    @Before
    public void initTest() {

        /*
            we want to have a new session, otherwise the tests
            will share the same Session beans
         */
        driver.manage().deleteAllCookies();

        home = new IndexPO(driver, "localhost", port);

        home.toStartingPage();

        assertTrue("Failed to start from Home Page", home.isOnPage());
    }


    @Test
    public void testHomePage() {
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedOut());
    }

    @Test
    public void testCreateUser() {
        SignupPO signin = home.doSignup();
        assertTrue(signin.isOnPage());
        assertFalse(home.isOnPage());

        String userId = getUniqueId();
        String password = "foo";

        home = signin.createUser(userId, password);
        assertFalse(signin.isOnPage());
        assertTrue(home.isOnPage());

        assertTrue(home.getDriver().getPageSource().contains(userId));
        assertTrue(home.isLoggedIn());
    }

    @Test
    public void testProtectedResource(){

        assertTrue(home.isLoggedOut());

        ProtectedPO protectedPO = home.goToProtectedPage();
        assertNull(protectedPO);

        SignupPO signin = home.doSignup();
        home = signin.createUser(getUniqueId(), "foo");

        assertTrue(home.isLoggedIn());

        protectedPO = home.goToProtectedPage();
        assertTrue(protectedPO.isOnPage());

        home = protectedPO.doLogout();
        assertTrue(home.isLoggedOut());
    }


    @Test
    public void testSignInLogOutLogIn(){

        assertTrue(home.isLoggedOut());

        SignupPO signin = home.doSignup();
        String userId = getUniqueId();
        String password = "foo";
        home = signin.createUser(userId, password);

        assertTrue(home.isLoggedIn());

        home.doLogout();

        assertTrue(home.isLoggedOut());

        LoginPO login = home.doLogin();
        home = login.loginUser(userId, password);

        assertTrue(home.isLoggedIn());
    }
}