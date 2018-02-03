package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.tsdes.misc.testutils.selenium.PageObject;

import java.util.List;

public class Ex03PO extends TemplatePO {


    public Ex03PO(PageObject po) {
        super(po);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Example using JSTL");
    }


    public int extractIndex(int position){
        String comment = getCommentText(position);
        return Integer.parseInt(comment.split(" ")[1]);
    }

    public void createNewComment(String comment){
        WebElement textArea = getDriver().findElement(By.id("createForm:createText"));
        textArea.clear();
        textArea.sendKeys(comment);
        WebElement button = getDriver().findElement(By.id("createForm:createButton"));
        button.click();

        waitForPageToLoad();
    }

    public int getNumberOfComments(){
        /*
            Bit tricky: here we need to use XPath:

            http://www.w3schools.com/xsl/xpath_syntax.asp

            The idea is that we define a "query" over the HTML/XML web page, and then
            get back a reference to all the XML tags / web elements satisfying such query.
            Here, we are saying:

            //table  :  any "table" tag in the document
            [@id='commentTable'] : among the <table> tags, just consider the one with id 'commentTable'
            //tbody : just consider <tbody> under the selected table(s), and not in any position in the document
            //tr[string-length(text()) > 0] : get the <tr> (table row) that has non-empty text content

            So to check how many comments there are on the page, we can look at how many rows exist
            in the table that is showing them.

            Note: a good way to try XPath is to run them directly in the browser. Eg, in
             Chrome go under "Developer tools", and when you "search" in the Elements view
             you can use XPath
         */
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='commentTable']//tbody//tr[string-length(text()) > 0]"));
        return elements.size();
    }

    public String getCommentText(int position){

        String htmlPos = "" + (position + 1);// XPath starts from 1 and not 0

        WebElement comment = getDriver().findElement(
                By.xpath("//table[@id='commentTable']//tbody//tr["+htmlPos+"]/td[2]"));

        return comment.getText();
    }


    public void deleteComment(int position){
        String htmlPos = "" + (position + 1);// XPath starts from 1 and not 0

        WebElement button = getDriver().findElement(
                By.xpath("//table[@id='commentTable']//tbody//tr["+htmlPos+"]/td[3]/form/input[@value = 'Delete']"));
        button.click();

        waitForPageToLoad();
    }
}
