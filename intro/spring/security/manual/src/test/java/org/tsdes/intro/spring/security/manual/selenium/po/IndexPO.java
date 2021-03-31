package org.tsdes.intro.spring.security.manual.selenium.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.tsdes.misc.testutils.selenium.PageObject;

import java.util.List;

public class IndexPO extends PageObject {

    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public IndexPO(PageObject other){
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login Example");
    }

    public void toStartingPage() {
        getDriver().get("http://"+host+":"+port);
        waitForPageToLoad();
        doLogout();
    }

    public boolean isLoggedIn(){
        /*
            When dealing with an element that might be missing, should use "findElements"
            rather than "findElement", as this latter does throw an exception if the
            element is missing.
         */
        return getDriver().findElements(By.id("logoutForm:logoutButton")).size() > 0;
    }

    public boolean isLoggedOut(){
        return getDriver().findElements(By.id("loginButton")).size() > 0 ;
    }

    public void doLogout(){
        if(isLoggedIn()){
            clickAndWait("logoutForm:logoutButton");
        }
    }

    /*
        As the login will lead us to a new page, in this method we do return the Page Object
        for such new page, or null if the page transition did not take place
     */
    public LoginPO doLogin(){

        if(isLoggedIn()){
            return null;
        }

        clickAndWait("loginButton");

        LoginPO po = new LoginPO(this);

        return po;
    }

    public int getNumberOfPosts(){

        return getDriver()
                .findElements(By.xpath("//div[@id = 'posts']//div[@class = 'post']"))
                .size();
    }

    public int getNumberOfPostsThatCanBeDeleted(){

        return getDriver()
                .findElements(By.xpath("//div[@id = 'posts']//div[@class = 'post']//div[@class = 'deleteButton']"))
                .size();
    }

    public boolean createPost(String text){

        List<WebElement> input = getDriver().findElements(By.id("postForm:postText"));
        if(input.isEmpty()){
            //can happen if not logged in
            return false;
        }

        setText("postForm:postText", text);
        clickAndWait("postForm:createPost");

        return true;
    }
}
