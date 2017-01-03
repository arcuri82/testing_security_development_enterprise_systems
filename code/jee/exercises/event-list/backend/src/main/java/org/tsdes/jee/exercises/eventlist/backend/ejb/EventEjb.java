package org.tsdes.jee.exercises.eventlist.backend.ejb;





import org.tsdes.jee.exercises.eventlist.backend.Countries;
import org.tsdes.jee.exercises.eventlist.backend.entity.Event;
import org.tsdes.jee.exercises.eventlist.backend.entity.User;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EventEjb {

    @PersistenceContext
    private EntityManager em;


    public Long createEvent(String title, String text, String country, String location, String userId){

        if(!Countries.getCountries().contains(country)){
            throw new IllegalArgumentException("Invalid country: "+country);
        }
        User user = em.find(User.class, userId);
        if(user == null){
            throw new IllegalArgumentException("No user with id: "+userId);
        }


        Event event = new Event();
        event.setTitle(title);
        event.setText(text);
        event.setCountry(country);
        event.setLocation(location);
        event.setAuthor(userId);

        em.persist(event);

        return event.getId();
    }


    public List<Event> getAllEvents(){
        return em.createNamedQuery(Event.GET_ALL).getResultList();
    }


    public List<Event> getEvents(String country){
        return em.createNamedQuery(Event.GET_COUNTRY).setParameter("fcountry",country).getResultList();
    }


}
