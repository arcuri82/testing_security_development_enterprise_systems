package org.tsdes.jee.exercises.eventlist.frontend;



import org.tsdes.jee.exercises.eventlist.backend.ejb.EventEjb;
import org.tsdes.jee.exercises.eventlist.backend.ejb.UserEJB;
import org.tsdes.jee.exercises.eventlist.backend.entity.Event;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@SessionScoped
public class EventController implements Serializable{

    private boolean showOnlyOwnCountry;
    private Map<Long, Boolean> attendMap;

    @EJB
    private EventEjb eventEjb;
    @EJB
    private UserEJB userEJB;
    @Inject
    private LoggingController loggingController;


    @PostConstruct
    public void init(){
        showOnlyOwnCountry = true;
        attendMap = new ConcurrentHashMap<>();
    }


    public List<Event> getEvents(){

        List<Event> events;

        if(! loggingController.isLoggedIn() || ! showOnlyOwnCountry){
            events = eventEjb.getAllEvents();
        } else {
            events = eventEjb.getEvents(loggingController.getUserCountry());
        }

        String registeredUser = loggingController.getRegisteredUser();
        if(registeredUser != null){
            events.stream().map(Event::getId)
                    .forEach(id -> {
                        if(userEJB.isUserAttendingEvent(registeredUser, id)) {
                            attendMap.put(id, true);
                        } else {
                            attendMap.put(id, false);
                        }
            });
        }

        return events;
    }

    public Map<Long, Boolean> getAttendMap() {
        return attendMap;
    }

    public void updateAttendance(Long id, Boolean attend){

        if(attend != null && attend){
           userEJB.addEvent(loggingController.getRegisteredUser(), id);
        } else {
            userEJB.removeEvent(loggingController.getRegisteredUser(), id);
        }
    }

    public boolean isShowOnlyOwnCountry() {
        return showOnlyOwnCountry;
    }

    public void setShowOnlyOwnCountry(boolean showOnlyOwnCountry) {
        this.showOnlyOwnCountry = showOnlyOwnCountry;
    }

}
