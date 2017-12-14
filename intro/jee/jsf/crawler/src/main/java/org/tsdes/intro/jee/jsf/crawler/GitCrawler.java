package org.tsdes.intro.jee.jsf.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
    For XPath syntax, see:
    http://www.w3schools.com/xsl/xpath_syntax.asp
 */


/*
    Do crawl Github in search of Java projects using Selenium.

    Note: this is just an example, as Github provides a REST-api which would be
    better in this context.

    Note 2: the code here depends on the HTML of the pages returned by Github.
    If those do change, then the code here will be broken... and this has
    actually happened in the past
 */
public class GitCrawler {

    private static final String github = "https://github.com/";

    private static boolean tryToSetGeckoIfExists(String property, Path path){
        if(Files.exists(path)){
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static void setupDriverExecutable(String executableName, String property){
        String homeDir = System.getProperty("user.home");

        //first try Linux/Mac executable
        if(! tryToSetGeckoIfExists(property, Paths.get(homeDir,executableName))){
            //then check if on Windows
            if(! tryToSetGeckoIfExists(property, Paths.get(homeDir,executableName+".exe"))){
                throw new RuntimeException("Cannot locate the "+executableName+" in your home directory "+homeDir);
            }
        }
    }

    public static void main(String[] args) {

        setupDriverExecutable("chromedriver", "webdriver.chrome.driver");
        WebDriver driver =  new ChromeDriver();

        try {
            crawl(driver);
        } catch (Exception e){
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
        }

        driver.close();
    }

    private static void openPagedSearchResult(WebDriver driver, int page){
        //Java projects, Most Stars
        String search =  "search?l=&o=desc&p="+page+"&q=language%3AJava&ref=advsearch&s=stars&type=Repositories&utf8=✓";

        driver.get(github + search);
    }

    private static void crawl(WebDriver driver) {

        for(int i=1; i <= 100; i++) {
            System.out.println("Page: "+i);
            openPagedSearchResult(driver, i);
            scanCurrentPage(driver, i);

            if(i==100){
                //a search returns at most 100 pages. so, to continue, we would need a new search
                break;
            }
            nextPage(driver, i);
        }
    }

    private static WebElement getElement(WebDriver driver, By by, int current){

        WebElement element = null;

        while(true) {
            try {
                element = driver.findElement(by);
            } catch (Exception e){
                //might happen due to Github blocking crawling
                try {
                    long time = 60_000;
                    System.out.println("Cannot find -> "+by.toString()+"\n Going to wait for "+time+"ms");
                    Thread.sleep(time);
                    openPagedSearchResult(driver, current);
                } catch (InterruptedException e1) {
                }
                continue;
            }
            break;
        }

        return element;
    }

    private static void nextPage(WebDriver driver, int current) {

        WebElement next = getElement(driver, By.xpath("//a[@class='next_page']"), current);
        next.click();
        waitForPageToLoad(driver);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    private static void scanCurrentPage(WebDriver driver, int page) {
        String current = driver.getCurrentUrl();

        //Note: there was a layout change in the past on Github page
        //String xpath = "//h3[@class='repo-list-name']/a";
        String xpath = "//div[@class='container']//ul//h3//a";

        List<WebElement> projects = driver.findElements(By.xpath(xpath));
        List<String> names = projects.stream().map(p -> p.getText()).collect(Collectors.toList());

        System.out.println("Going to analyze "+names.size()+" projects in this page at: "+current);

        while (!names.isEmpty()) {

            String name = names.remove(0);

            try {
                /*
                    do not overflow GitHub of requests. 10 per minutes should be more than enough.
                    Recall: "If you would like to crawl GitHub contact us at support@github.com."
                    from  https://github.com/robots.txt
                 */
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }

            By byName = By.xpath(xpath + "[text()='" + name + "']");
            WebElement a = getElement(driver, byName, page);
            a.click();
            Boolean loaded = waitForPageToLoad(driver);

            if (loaded) {

                /*
                    Checking if either Maven or Gradle

                    Note: not a robust check:
                    - build files might not be in root folder
                    - no check if there was any error in the loaded page
                 */
                List<WebElement> maven = driver.findElements(By.xpath("//td[@class='content']//a[@title='pom.xml']"));
                if (!maven.isEmpty()) {
                    System.out.println("" + name + " uses Maven");
                } else {
                    List<WebElement> gradle =  driver.findElements(By.xpath("//td[@class='content']//a[@title='build.gradle']"));
                    if (!gradle.isEmpty()) {
                        System.out.println("" + name + " uses Gradle");
                    } else {
                        System.out.println("" + name + " undefined build system");
                    }
                }
            }

            driver.get(current);
            waitForPageToLoad(driver);
        }
    }

    private static Boolean waitForPageToLoad(WebDriver driver) {

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }

}
