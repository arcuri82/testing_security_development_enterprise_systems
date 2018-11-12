package org.tsdes.intro.exercises.quizgame.selenium;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.exercises.quizgame.Application;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT extends SeleniumTestBase {

    private static WebDriver driver;

    @LocalServerPort
    private int port;


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

    @Override
    protected WebDriver getDriver() {
        return driver;
    }

    @Override
    protected String getServerHost() {
        return "localhost";
    }

    @Override
    protected int getServerPort() {
        return port;
    }
}
