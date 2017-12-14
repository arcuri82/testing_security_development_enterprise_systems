package org.tsdes.intro.jee.ejb.time.businesslayer;

import javax.annotation.PostConstruct;
import javax.ejb.*;

@Singleton
@Startup //initialize it immediately at startup, ie don't wait for its first access
public class AuthorBot {

    public static final String BAR = "Bar";
    public static final String MON = "Mon";
    public static final String THU = "Thu";

    public static final String POST_CONSTRUCT = "AuthorBot_init()";


    @EJB
    private NewsEJB newsEJB;


    @PostConstruct
    private void init(){
        /*
            Has to be in a post-constructor instead of directly in a constructor because
            we are using a EJB that has to be first injected before being usable
         */
        newsEJB.createNews(POST_CONSTRUCT, "@PostConstruct is called after a bean has been created");
    }

    /*
        Careful of default values, it is 0 for seconds/minutes/hours and not *
     */

    @Schedule(second = "*/2" , minute="*", hour="*", persistent=false)
    public void createBar(){
        if(canCreateNews()) {
            newsEJB.createNews(BAR, "Some text");
        }
    }


    @Schedule(second = "*/3", minute="*", hour="*", dayOfWeek = "Thu", persistent=false)
    public void createSun(){
        if(canCreateNews()) {
            newsEJB.createNews(THU, "Some text");
        }
    }

    @Schedule(second = "*/2", minute="*", hour="*", dayOfWeek = "Mon", persistent=false)
    public void createMon(){
        if(canCreateNews()) {
            newsEJB.createNews(MON, "Some text");
        }
    }

    private boolean canCreateNews(){
        if(newsEJB.getAllNews().size() >= 10){
            return false;
        }
        return true;
    }
}
