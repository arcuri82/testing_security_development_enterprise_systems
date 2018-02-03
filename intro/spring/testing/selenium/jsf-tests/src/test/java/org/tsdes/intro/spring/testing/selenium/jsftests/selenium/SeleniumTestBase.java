package org.tsdes.intro.spring.testing.selenium.jsftests.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po.Ex01PO;
import org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po.Ex03PO;
import org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po.HomePO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class SeleniumTestBase {

    protected abstract WebDriver getDriver();

    protected abstract String getServerHost();

    protected abstract int getServerPort();


    private HomePO home;

    @Before
    public void initTest(){
        home = new HomePO(getDriver(), getServerHost(), getServerPort());

        home.toStartingPage();

        assertTrue("Failed to start from Home Page", home.isOnPage());
    }


    //--- Ex 01 ----------------------------------------------------------------

    @Test
    public void testEx01Incr() {
        Ex01PO po = home.toEx01Page();

        int x = po.getCounterValue();
        po.clickPlus();
        int res = po.getCounterValue();
        assertEquals(x + 1, res);
    }

    @Test
    public void testEx01FailedDecr() {
        Ex01PO po = home.toEx01Page();

        po.clickReset();
        int res = po.getCounterValue();
        assertEquals(0, res);
        po.clickMinus();
        res = po.getCounterValue();
        assertEquals(0, res);
    }

    @Test
    public void testEx01Decr() {
        Ex01PO po = home.toEx01Page();

        int x = po.getCounterValue();
        po.clickPlus();
        assertEquals(x + 1, (int) po.getCounterValue());
        po.clickMinus();
        assertEquals(x, (int) po.getCounterValue());
    }

    @Test
    public void testEx01IncrAndDecr() {
        Ex01PO po = home.toEx01Page();

        po.clickReset(); //0
        po.clickMinus(); //no effect
        po.clickPlus();  // 1
        po.clickPlus();  // 2
        po.clickMinus(); // 1
        po.clickPlus();  // 2
        assertEquals(2, (int) po.getCounterValue());
    }


    //--- Ex 03 ----------------------------------------------------------------

     /*
        Recall, for this kind of tests were the web application is started just once,
        when I run a test I cannot make assumptions on the current state (eg databases
        and Singleton EJBs).
        So, to still make the tests "independent", need to write them in a way in
        which they should be valid regardless of the current state of the application.
     */

     //FIXME
    private final int MAX = 10; // the max value of comments in the table

    @Test
    public void testEx03Create(){
        Ex03PO po = home.toEx03Page();

        String text = "testCreate() foo"; //just use something unique for this test

        int n = po.getNumberOfComments(); //cannot make assumptions on "n" at this point
        po.createNewComment(text);

        int k = po.getNumberOfComments();

        //either we already had the maximum, or should had been increased by 1
        if(n==MAX){
            assertEquals(n , k);
        } else {
            assertEquals(n+1, k);
        }

        //new comment should always be on top, in position 0
        assertEquals(text, po.getCommentText(0));
    }


    @Test
    public void testEx03Delete(){
        Ex03PO po = home.toEx03Page();

        String baseText = "testDelete() ";

        //fill table with new comments
        for(int i=0; i<MAX; i++) {
            int index = (MAX-1) - i; //want to insert 9,8,7,..,0 so that 0,1,2,... is on top
            po.createNewComment(baseText + index);
        }

        //just make sure first and last are correct
        assertEquals(0, po.extractIndex(0));
        assertEquals(9, po.extractIndex(9));

        po.deleteComment(0);
        //all should have shift position
        assertEquals(1, po.extractIndex(0));
        assertEquals(9, po.extractIndex(8));

        po.deleteComment(1);
        assertEquals(1, po.extractIndex(0)); // this should had stayed the same, as removing after it
        assertEquals(3, po.extractIndex(1));
        assertEquals(9, po.extractIndex(7));
    }




//    @BeforeClass
//    public static void init() throws InterruptedException {
//
//        //driver = getFirefoxDriver(); //not so stable
//        driver = getChromeDriver();
//
//
//        /*
//            When the integration tests in this class are run, it might be
//            that WildFly is not ready yet, although it was started. So
//            we need to wait till it is ready.
//         */
//        for(int i=0; i<30; i++){
//            boolean ready = false; //FIXME JBossUtil.isJBossUpAndRunning();
//            if(!ready){
//                Thread.sleep(1_000); //check every second
//                continue;
//            } else {
//                break;
//            }
//        }
//    }
//
//    protected WebDriver getDriver(){
//        return driver;
//    }
//
//    @AfterClass
//    public static void tearDown(){
//        driver.close();
//    }
//
//
//    @Before
//    public void checkIfWildflyIsRunning(){
//
//        //if for any reason WildFly is not running any more, do not fail the tests
//        //assumeTrue("Wildfly is not up and running", JBossUtil.isJBossUpAndRunning());
//        //FIXME
//    }
}
