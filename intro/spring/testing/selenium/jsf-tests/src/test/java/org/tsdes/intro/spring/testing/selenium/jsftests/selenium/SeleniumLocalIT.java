package org.tsdes.intro.spring.testing.selenium.jsftests.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.spring.jsf.Application;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT extends SeleniumTestBase{

    private static WebDriver driver;

    @LocalServerPort
    private int port;


    @BeforeAll
    public static void initClass(){

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver != null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
    public static void tearDown() {
        if(driver != null) {
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
