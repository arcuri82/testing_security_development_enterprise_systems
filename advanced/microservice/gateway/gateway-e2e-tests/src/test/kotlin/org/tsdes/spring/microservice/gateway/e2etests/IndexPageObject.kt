package org.tsdes.spring.microservice.gateway.e2etests

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait


class IndexPageObject(
    private val driver: WebDriver
){

    fun goToPage(host:String, port: Int){
        driver.get("http://$host:$port")
        waitForPageToLoad()
    }

    fun isOnPage() = driver.title.contains("Zuul")

    fun sendMessage(msg: String, wait: Boolean = true){

        val n = numberOfMessages()

        val btn = driver.findElement(By.id("sendBtnId"))
        val area = driver.findElement(By.id("messageAreaId"))

        area.clear()
        area.sendKeys(msg)
        btn.click()

        if(wait) {
            waitForDisplayedMessages(n + 1)
        }
    }

    fun deleteMessages(){

        val btn = driver.findElement(By.id("deleteBtnId"))
        btn.click()

        waitForDisplayedMessages(0)
    }

    fun numberOfMessages() : Int{

        val messages = driver.findElements(By.xpath("//div[@id='messagesId']//li"))

        return messages.size
    }

    fun messages() : List<String>{

        while(true) {
            try {
                val lis = driver.findElements(By.xpath("//div[@id='messagesId']//li"))

                return lis.map { it.text }
            } catch (e: StaleElementReferenceException){
                /*
                    in case a callback change the list while we are
                    reading it.
                 */
            }
        }
    }

    private fun waitForDisplayedMessages(n: Int){
        val wait = WebDriverWait(driver, 10)

        wait.until({numberOfMessages() == n})

    }

    private fun waitForPageToLoad() {
        val wait = WebDriverWait(driver, 10) //give up after 10 seconds

        wait.until({(it as JavascriptExecutor).executeScript("return document.readyState") == "complete"})
    }
}