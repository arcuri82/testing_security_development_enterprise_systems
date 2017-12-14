package org.tsdes.jee.exercises.eventlist.backend.ejb;


import org.apache.commons.codec.digest.DigestUtils;
import org.tsdes.jee.exercises.eventlist.backend.entity.Event;
import org.tsdes.jee.exercises.eventlist.backend.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;


@Stateless
public class UserEJB implements Serializable{

    @PersistenceContext
    private EntityManager em;

    public UserEJB(){
    }

    public void removeEvent(String userId, Long eventId){

        User user = em.find(User.class, userId);

        Iterator<Event> iterEvent = user.getEventsToAttend().iterator();
        while(iterEvent.hasNext()){
            Event current = iterEvent.next();
            if(current.getId().equals(eventId)){
                iterEvent.remove();

                Iterator<User> iterUser = current.getAttendingUsers().iterator();
                while(iterUser.hasNext()){
                    User k = iterUser.next();
                    if(userId.equals(k.getUserId())){
                        iterUser.remove();
                        break;
                    }
                }

                break;
            }
        }
    }

    public void addEvent(String userId, Long eventId){
        User user = em.find(User.class, userId);
        Event event = em.find(Event.class, eventId);

        if(user.getEventsToAttend().stream().anyMatch(e -> e.getId().equals(eventId))){
            return;
        }

        user.getEventsToAttend().add(event);
        event.getAttendingUsers().add(user);
    }

    /**
     *
     * @return {@code false} if for any reason it was not possible to create the user
     */
    public boolean createUser(String userId, String password, String firstName, String middleName, String lastName, String country) {
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User user = getUser(userId);
        if (user != null) {
            //a user with same id already exists
            return false;
        }

        user = new User();
        user.setUserId(userId);

        //create a "strong" random string of at least 128 bits, needed for the "salt"
        String salt = getSalt();
        user.setSalt(salt);

        String hash = computeHash(password, salt);
        user.setHash(hash);

        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setCountry(country);

        em.persist(user);

        return true;
    }


    /**
     *
     * @param userId
     * @param password
     * @return  {@code true} if a user with the given password exists
     */
    public boolean login(String userId, String password) {
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User userDetails = getUser(userId);
        if (userDetails == null) {
            return false;
        }

        String hash = computeHash(password, userDetails.getSalt());

        boolean isOK = hash.equals(userDetails.getHash());
        return isOK;
    }


    public User getUser(String userId){
        return em.find(User.class, userId);
    }


    public boolean isUserAttendingEvent(String userId, Long eventId){

        User user = getUser(userId);
        if(user == null){
            return false;
        }

        return  user.getEventsToAttend().stream().anyMatch(e -> e.getId().equals(eventId));
    }


    //------------------------------------------------------------------------


    @NotNull
    protected String computeHash(String password, String salt){
        String combined = password + salt;
        String hash = DigestUtils.sha256Hex(combined);
        return hash;
    }

    @NotNull
    protected String getSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        return salt;
    }
}
