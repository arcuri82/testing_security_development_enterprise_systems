package org.tsdes.jee.exercises.mycantina.frontend;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

/**
 * Created by arcuri82 on 28-Nov-17.
 */
@RunWith(Arquillian.class)
public class SeleniumLocalIT extends SeleniumTestBase {

    private static WebDriver driver;

    private static boolean tryToSetGeckoIfExists(String property, Path path) {
        if (Files.exists(path)) {
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static void setupDriverExecutable(String executableName, String property) {
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName))) {
            //then check if on Windows
            if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName + ".exe"))) {
                fail("Cannot locate the " + executableName + " in your home directory " + homeDir);
            }
        }
    }

    private static WebDriver getChromeDriver() {

        /*
            Need to have Chrome (eg version 53.x) and the Chrome Driver (eg 2.24),
            whose executable should be saved directly under your home directory

            see https://sites.google.com/a/chromium.org/chromedriver/getting-started
         */

        setupDriverExecutable("chromedriver", "webdriver.chrome.driver");

        return new ChromeDriver();
    }


    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        String name = "my-cantina-frontend-0.0.1-SNAPSHOT.war";
        return ShrinkWrap.create(ZipImporter.class, name)
                .importFrom(new File("target/" + name))
                .as(WebArchive.class);
    }



    @Override
    protected WebDriver getDriver() {
        return driver;
    }

    @Override
    protected String getJeeHost() {
        return "localhost";
    }

    @Override
    protected int getJeePort() {
        return 8080;
    }

    @BeforeClass
    public static void init() throws InterruptedException {
        driver = getChromeDriver();
    }


    @AfterClass
    public static void tearDown() {
        driver.close();
    }

}
