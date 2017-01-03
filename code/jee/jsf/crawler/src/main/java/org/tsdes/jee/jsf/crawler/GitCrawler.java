package org.tsdes.jee.jsf.crawler;

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

    also XPath was mentioned in the book at page 392
 */


/*
    Do crawl Github in search of Java projects using Selenium.

    Note: this is just an example, as Github provides a REST-api which would be
    better in this context.

    Note2: the code here depends on the HTML of the pages returned by Github.
    If those do change, then the code here will be broken...
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


        int start = 1;
        if(args.length==1){
            start = Integer.parseInt(args[0]);
        }

        try {
            crawl(driver, start);
        } catch (Exception e){
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
        }

        driver.close();
    }

    private static void crawl(WebDriver driver, int start) {
        //Java projects, Most Stars
        int stars = 100000;
        String search =  "search?l=&p="+start+"&q=language%3Ajava+stars%3A<%3D"+stars+"&ref=advsearch&type=Repositories&utf8=✓";

        driver.get(github + search);

        for(int i=start; i <= 100; i++) {
            System.out.println("Page: "+i);
            scanCurrentPage(driver);

            if(i==100){
                //a search returns at most 100 pages. so, to continue, we would need a new search
                break;
            }
            nextPage(driver);
        }
    }

    private static void nextPage(WebDriver driver) {

        WebElement next = driver.findElement(By.xpath("//a[@class='next_page']"));
        next.click();
        waitForPageToLoad(driver);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    private static void scanCurrentPage(WebDriver driver) {
        String current = driver.getCurrentUrl();

        String xpath = "//h3[@class='repo-list-name']/a";
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

            WebElement a;
            while(true) {
                try {
                    a = driver.findElement(By.xpath(xpath + "[text()='" + name + "']"));
                } catch (Exception e){
                    //might happen due to Github blocking crawling
                    try {
                        long time = 60_000;
                        System.out.println("Cannot find "+name+". Going to wait for "+time+"ms");
                        Thread.sleep(time);
                        driver.get(current);
                        waitForPageToLoad(driver);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                break;
            }
            a.click();
            Boolean loaded = waitForPageToLoad(driver);

            if (loaded) {

                List<WebElement> pom = driver.findElements(By.xpath("//td[@class='content']//a[@title='pom.xml']"));
                if (!pom.isEmpty()) {
                    boolean valid = isValidPom(pom.get(0).getAttribute("href"));
                    if (valid) {
                        System.out.println("VALID PROJECT THAT IS USING Selenium AT: " + driver.getCurrentUrl());
                    } else{
                        System.out.println("NOT valid project at: " + driver.getCurrentUrl());
                    }
                }
            }

            driver.get(current);
            waitForPageToLoad(driver);
        }
    }

    //does the target project contain any reference to Selenium in its pom.xml file?
    private static boolean isValidPom(String href) {

        String url = href;
        if(!url.startsWith(github)){
            url = github + href;
        }

        System.out.println("Going to download: "+url);

        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            connection.connect();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            StringBuffer buffer = new StringBuffer();
            Scanner scanner = new Scanner(in);
            while(scanner.hasNextLine()){
                buffer.append(scanner.nextLine());
                buffer.append("\n");
            }
            String content = buffer.toString();

            return content.contains("selenium");

        } catch (Exception e) {
            System.out.println("Failed to read pom.xml from " + url + " : " + e.getMessage());
            return false;
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
