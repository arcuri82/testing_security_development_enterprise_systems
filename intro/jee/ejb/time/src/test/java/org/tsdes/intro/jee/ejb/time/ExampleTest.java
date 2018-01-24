package org.tsdes.intro.jee.ejb.time;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.intro.jee.ejb.time.businesslayer.AuthorBot;
import org.tsdes.intro.jee.ejb.time.businesslayer.CommentatorBot;
import org.tsdes.intro.jee.ejb.time.businesslayer.NewsEJB;
import org.tsdes.intro.jee.ejb.time.datalayer.News;

import javax.ejb.EJB;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
        Note: this has to be run with Arquillian, because timing service
        is not handled by the embedded JEE containers
*/
@RunWith(Arquillian.class)
public class ExampleTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.intro")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private NewsEJB newsEJB;

    @Test
    public void test() throws Exception{

        Thread.sleep(4_000); //give some time to generate some messages/comments

        List<News> news = newsEJB.getAllNews();

        //should be at least 1 news, ie check if time service did at least started
        assertFalse(news.isEmpty());

        assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.POST_CONSTRUCT)));
        assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.BAR)));


        /*
            Some news should had been created only on Mondays, whereas others only
            on Thursdays. This check does actually depend on the day of the week
            in which this test is run
         */
        if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.MON)));
            assertFalse(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.THU)));
        } else if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.THU)));
            assertFalse(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.MON)));
        }


        /*
            Note the use of "flatMap" here... ie, from a stream of News we map to
            a stream of Comment coming from all of the news
         */
        long comments = news.stream().flatMap(n -> n.getComments().stream())
                .filter(c -> c.getAuthor().equals(CommentatorBot.COMMENTATOR)).count();

        assertTrue(comments>0); //at least one Comment
    }
}
