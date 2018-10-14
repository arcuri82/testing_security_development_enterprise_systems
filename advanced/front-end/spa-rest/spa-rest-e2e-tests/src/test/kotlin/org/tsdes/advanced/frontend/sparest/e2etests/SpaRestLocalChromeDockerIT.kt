package org.tsdes.advanced.frontend.sparest.e2etests

import org.junit.AfterClass
import org.junit.AssumptionViolatedException
import org.junit.BeforeClass
import org.openqa.selenium.WebDriver
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler


class SpaRestLocalChromeDockerIT : SpaRestSeleniumTestBase() {

    companion object {

        var driver: WebDriver? = null

        @BeforeClass
        @JvmStatic
        fun initClass() {

            driver = SeleniumDriverHandler.getChromeDriver()

            if (driver == null) {
                //Do not fail the tests.
                throw AssumptionViolatedException("Cannot find/initialize Chrome driver")
            }
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            if (driver != null) {
                driver!!.close()
            }
        }
    }

    override fun getDriver(): WebDriver {
        return driver!!
    }

    override fun getServerHost(): String {
        return "localhost"
    }

    override fun getServerPort(): Int {
        return 8080
    }

}