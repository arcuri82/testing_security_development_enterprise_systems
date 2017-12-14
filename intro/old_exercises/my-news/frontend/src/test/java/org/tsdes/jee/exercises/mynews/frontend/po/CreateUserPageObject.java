package org.tsdes.jee.exercises.mynews.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

public class CreateUserPageObject extends PageObject {

    public CreateUserPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Create User", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Create User");
    }

    public HomePageObject createUser(String userId, String password, String confirmPassword,
                                     String firstName, String middleName, String lastName){

        setText("createUserForm:userName",userId);
        setText("createUserForm:password",password);
        setText("createUserForm:confirmPassword",confirmPassword);
        setText("createUserForm:firstName",firstName);
        setText("createUserForm:middleName",middleName);
        setText("createUserForm:lastName",lastName);


        driver.findElement(By.id("createUserForm:createButton")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }
}
