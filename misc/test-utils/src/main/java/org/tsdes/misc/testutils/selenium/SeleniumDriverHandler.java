package org.tsdes.misc.testutils.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by arcuri82 on 02-Feb-18.
 */
public class SeleniumDriverHandler {


    private static boolean tryToSetGeckoIfExists(String property, Path path) {
        if (Files.exists(path)) {
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static boolean setupDriverExecutable(String executableName, String property) {
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName))) {
            //then check if on Windows
            if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName + ".exe"))) {
                System.out.println("WARNING: Cannot locate the " + executableName + " in your home directory " + homeDir);
                return false;
            }
        }

        return true;
    }

    public static WebDriver getChromeDriver() {

        /*
            Need to have Chrome (eg version 53.x) and the Chrome Driver (eg 2.24),
            whose executable should be saved directly under your home directory

            see https://sites.google.com/a/chromium.org/chromedriver/getting-started
         */

        boolean isOk = setupDriverExecutable("chromedriver", "webdriver.chrome.driver");
        if(! isOk){
            return null;
        }

        return new ChromeDriver();
    }

    public static WebDriver getFirefoxDriver(){
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

}
