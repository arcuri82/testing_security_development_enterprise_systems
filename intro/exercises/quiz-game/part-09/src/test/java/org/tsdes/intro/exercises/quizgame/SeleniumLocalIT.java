package org.tsdes.intro.exercises.quizgame;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.exercises.quizgame.po.IndexPO;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = RANDOM_PORT)
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


    private IndexPO home;

    @Before
    public void initTest(){
        home = new IndexPO(driver, "localhost", port);

        home.toStartingPage();

        assertTrue("Failed to start from Home Page", home.isOnPage());
    }



}
