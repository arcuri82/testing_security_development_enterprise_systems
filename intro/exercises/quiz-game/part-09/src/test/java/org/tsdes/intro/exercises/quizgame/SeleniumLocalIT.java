package org.tsdes.intro.exercises.quizgame;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.exercises.quizgame.po.IndexPO;
import org.tsdes.intro.exercises.quizgame.po.ui.MatchPO;
import org.tsdes.intro.exercises.quizgame.po.ui.ResultPO;
import org.tsdes.intro.exercises.quizgame.service.QuizService;
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumLocalIT {

    private static WebDriver driver;

    @LocalServerPort
    private int port;

    @Autowired
    private QuizService quizService;


    @BeforeAll
    public static void initClass() {

        driver = SeleniumDriverHandler.getChromeDriver();

        assumeTrue(driver != null, "Cannot find/initialize Chrome driver");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }


    private IndexPO home;

    @BeforeEach
    public void initTest() {

        /*
            we want to have a new session, otherwise the tests
            will share the same Session beans
         */
        driver.manage().deleteAllCookies();

        home = new IndexPO(driver, "localhost", port);

        home.toStartingPage();

        assertTrue(home.isOnPage(), "Failed to start from Home Page");
    }

    @Test
    public void testNewMatch() {

        MatchPO po = home.startNewMatch();
        assertTrue(po.canSelectCategory());
    }

    @Test
    public void testFirstQuiz() {

        MatchPO po = home.startNewMatch();
        String ctgId = po.getCategoryIds().get(0);

        assertTrue(po.canSelectCategory());
        assertFalse(po.isQuestionDisplayed());

        po.chooseCategory(ctgId);
        assertFalse(po.canSelectCategory());
        assertTrue(po.isQuestionDisplayed());

        assertEquals(1, po.getQuestionCounter());
    }

    @Test
    public void testWrongAnswer() {

        MatchPO matchPO = home.startNewMatch();
        String ctgId = matchPO.getCategoryIds().get(0);

        matchPO.chooseCategory(ctgId);

        long quizId = matchPO.getQuizId();

        int rightAnswer = quizService.getQuiz(quizId).getIndexOfCorrectAnswer();
        int wrongAnswer = (rightAnswer + 1) % 4;

        ResultPO resultPO = matchPO.answerQuestion(wrongAnswer);
        assertNotNull(resultPO);

        assertTrue(resultPO.haveLost());
        assertFalse(resultPO.haveWon());
    }

    @Test
    public void testWinAMatch() {

        MatchPO matchPO = home.startNewMatch();
        String ctgId = matchPO.getCategoryIds().get(0);
        matchPO.chooseCategory(ctgId);

        ResultPO resultPO = null;

        for (int i = 1; i <= 5; i++) {
            assertTrue(matchPO.isQuestionDisplayed());
            assertEquals(i, matchPO.getQuestionCounter());

            long quizId = matchPO.getQuizId();
            int rightAnswer = quizService.getQuiz(quizId).getIndexOfCorrectAnswer();

            resultPO = matchPO.answerQuestion(rightAnswer);

            if(i != 5) {
                assertNull(resultPO);
            }
        }

        assertTrue(resultPO.haveWon());
        assertFalse(resultPO.haveLost());
    }
}
