package org.tsdes.intro.spring.testing.coverage.jacoco.frontend;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.testing.coverage.jacoco.Application;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {

    private static WebDriver driver;

    @LocalServerPort
    private int port;


    @BeforeAll
    public static void initClass(){

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver!=null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
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