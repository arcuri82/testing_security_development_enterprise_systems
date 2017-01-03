package org.tsdes.jee.exercises.eventlist.backend.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.exercises.eventlist.backend.entity.Event;
import org.tsdes.jee.exercises.eventlist.backend.util.DeleterEJB;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
public class EventEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.jee.exercises.eventlist.backend")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private DeleterEJB deleterEJB;

    @EJB
    private EventEjb eventEjb;
    @EJB
    private UserEJB userEJB;

    @Before
    @After
    public void emptyDatabase(){
        deleterEJB.deleteEntities(Event.class);
    }


    @Test
    public void testNoEvents(){
        List<Event> list = eventEjb.getAllEvents();
        assertEquals(0, list.size());
    }

    @Test(expected = javax.ejb.EJBException.class)
    public void testCreateEventWrongCountry(){
        eventEjb.createEvent("title","text","FOO","","userId");
    }

    @Test(expected = javax.ejb.EJBException.class)
    public void testCreateEventWrongUserId(){
        eventEjb.createEvent("title","text","Norway","","aUserIdThatDoesNotExist");
    }

    @Test
    public void testCreateEvent(){
        assertEquals(0, eventEjb.getAllEvents().size());

        String country = "Norway";
        String userId = "Foo";
        userEJB.createUser(userId,"foo","foo","","foo",country);
        eventEjb.createEvent("title","text",country,"location",userId);

        assertEquals(1, eventEjb.getAllEvents().size());
        assertEquals(1, eventEjb.getEvents(country).size());
        assertEquals(0, eventEjb.getEvents(country+"foo").size());
    }
}