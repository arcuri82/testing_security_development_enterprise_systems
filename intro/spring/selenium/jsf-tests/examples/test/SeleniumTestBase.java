package org.tsdes.intro.jee.jsf.examples.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public abstract class SeleniumTestBase {

    private static WebDriver driver;

    private static boolean tryToSetDriverIfExists(String property, Path path){
        if(Files.exists(path)){
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static void setupDriverExecutable(String executableName, String property){
        //You need to download the drivers under your home directory
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if(! tryToSetDriverIfExists(property, Paths.get(homeDir,executableName))){
            //then check if on Windows
            if(! tryToSetDriverIfExists(property, Paths.get(homeDir,executableName+".exe"))){
                fail("Cannot locate the "+executableName+" in your home directory "+homeDir);
            }
        }
    }

    private static WebDriver getFirefoxDriver(){
        /*
            Need to have an updated Firefox, but also need
            to download and put the geckodriver in your own home dir.
            See:

            https://developer.mozilla.org/en-US/docs/Mozilla/QA/Marionette/WebDriver
            https://github.com/mozilla/geckodriver/releases

            However, drivers for FireFox have been often unstable.
            Therefore, I do recommend to use Chrome instead
         */

        setupDriverExecutable("geckodriver", "webdriver.gecko.driver");

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
        desiredCapabilities.setCapability("marionette", true);
        desiredCapabilities.setJavascriptEnabled(true);

        return  new FirefoxDriver(desiredCapabilities);
    }

    private static WebDriver getChromeDriver(){

        /*
            Need to have Chrome  and the Chrome Driver,
            whose executable should be saved directly under your home directory

            see https://sites.google.com/a/chromium.org/chromedriver/getting-started
         */

        setupDriverExecutable("chromedriver", "webdriver.chrome.driver");

        return new ChromeDriver();
    }

    @BeforeClass
    public static void init() throws InterruptedException {

        //driver = getFirefoxDriver(); //not so stable
        driver = getChromeDriver();


        /*
            When the integration tests in this class are run, it might be
            that WildFly is not ready yet, although it was started. So
            we need to wait till it is ready.
         */
        for(int i=0; i<30; i++){
            boolean ready = JBossUtil.isJBossUpAndRunning();
            if(!ready){
                Thread.sleep(1_000); //check every second
                continue;
            } else {
                break;
            }
        }
    }

    protected WebDriver getDriver(){
        return driver;
    }

    @AfterClass
    public static void tearDown(){
        driver.close();
    }


    @Before
    public void checkIfWildflyIsRunning(){

        //if for any reason WildFly is not running any more, do not fail the tests
        assumeTrue("Wildfly is not up and running", JBossUtil.isJBossUpAndRunning());
    }
}
