package org.tsdes.advanced.frontend.sparest.e2etests.po

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.tsdes.misc.testutils.selenium.PageObject


class HomePO(driver: WebDriver, host: String, port: Int) : PageObject(driver, host, port) {


    constructor(po: PageObject) : this(po.driver, po.host, po.port)

    fun toStartingPage() {
        driver.get("$host:$port")
    }

    override fun isOnPage(): Boolean {

        return waitForPageToLoad()
                && waitForVisibility(3, By.xpath("//h2[text()='Book List']"))
    }



}