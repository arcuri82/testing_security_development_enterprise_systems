package org.tsdes.intro.spring.testing.coverage.jacoco.frontend;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.spring.testing.coverage.jacoco.Application;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {

    private static WebDriver driver;

    @LocalServerPort
    private int port;


    @BeforeClass
    public static void initClass(){

        driver = SeleniumDriverHandler.getChromeDriver();

        if(driver == null){
            //Do not fail the tests.
            throw new AssumptionViolatedException("Cannot find/initialize Chrome driver");
        }
    }

    @AfterClass
    public static void tearDown() {
        if(driver != null) {
            driver.close();
        }
    }


    @Test
    public void testChangeData(){

        HomePageObject po = new HomePageObject(driver, "localhost", port);
        po.toStartingPage();
        assertTrue(po.isOnPage());

        String first = "foo data";
        po.changeData(first);
        assertTrue(driver.getPageSource().contains(first));

        String second = "bar data";
        po.changeData(second);
        String src = driver.getPageSource();
        assertTrue(src.contains(second));
        assertFalse(src.contains(first));
    }
}