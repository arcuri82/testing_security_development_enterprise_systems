package org.tsdes.jee.exercises.mynews.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.tsdes.jee.exercises.mynews.frontend.PostController;

import java.util.List;


public class HomePageObject extends PageObject {


    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public HomePageObject toStartingPage() {
        String context = "/pg5100_exam"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/mynews/home.jsf");
        waitForPageToLoad();

        return this;
    }

    public boolean isOnPage() {
        return driver.getTitle().equals("MyNews Home Page");
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }



    public int getNumberOfDisplayedPosts() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr/td/a"));

        return elements.size();
    }

    public int getNumberOfDiplayedPostsMadeByAUser(String userId){
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr/td/a[contains(@href, 'id="+userId+"')]"));

        return elements.size();
    }

    public UserDetailsPageObject toUserDetails(String userId){
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//tbody//tr/td/a[contains(@href, 'id="+userId+"')]"));

        if(elements.isEmpty()){
            return null;
        }

        WebElement link = elements.get(0);
        link.click();
        waitForPageToLoad();
        return new UserDetailsPageObject(driver);
    }

    public void createNewEvent(String text){

        setText("createForm:createText", text);
        WebElement button = driver.findElement(By.id("createForm:createButton"));
        button.click();
        waitForPageToLoad();
    }

    public boolean isVotingVisible(){

        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='postTable']//thead//tr/th[contains(text(), 'Your Vote')]"));

        return ! elements.isEmpty();
    }



    public void sortPostsBy(PostController.Sorting sorting){
        new Select(driver.findElement(By.id("sortForm:sorting"))).selectByVisibleText(sorting.toString());
    }

    public int getScore(int position){
        return Integer.parseInt(getText("postTable:"+position+":score"));
    }

    public void voteFor(int position){
        vote(position,"2");
    }

    public void voteAgainst(int position){
        vote(position,"0");
    }

    public void voteNone(int position){
        vote(position,"1");
    }

    public CommentsPageObject openNews(int position){
        WebElement link = driver.findElement(By.id("postTable:"+position+":linkForm:text"));
        link.click();
        waitForPageToLoad();

        return new CommentsPageObject(driver);
    }

    private void vote(int position, String label){
        WebElement radio = driver.findElement(By.id("postTable:"+position+":voteForm:radio:"+label));
        radio.click();
        waitForPageToLoad();
    }

    public String getPostText(int position){
        return getText("postTable:"+position+":linkForm:text");
    }

    public boolean arePostsSortedByScore(){

        int previous = Integer.MAX_VALUE;

        int n = getNumberOfDisplayedPosts();
        for(int i=0; i< n; i++){
            int score = getScore(i);

            if(score > previous){
                return false;
            }
            previous = score;
        }

        return true;
    }
}
