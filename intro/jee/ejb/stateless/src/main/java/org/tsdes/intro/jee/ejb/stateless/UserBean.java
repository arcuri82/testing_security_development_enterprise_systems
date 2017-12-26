package org.tsdes.intro.jee.ejb.stateless;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

/*
    A stateless EJB should not keep any internal state, eg fields, as the
    JEE container might create thousands of instances, each time serving
    potentially a different one.
 */
@Stateless
public class UserBean {

    //Dependency injection: the container will add it
    @PersistenceContext
    private EntityManager em;

    public UserBean(){}

    //all methods in a EJB are wrapped in a transaction, with automated rollback if exceptions
    public void registerNewUser(@NotNull String userId, @NotNull String name, @NotNull String surname){
        if(isRegistered(userId)){
            return;
        }

        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setSurname(surname);

        em.persist(user);
    }

    public boolean isRegistered(@NotNull String userId){
        User user = em.find(User.class, userId);
        return user != null;
    }

    public long getNumberOfUsers(){
        TypedQuery<Long> query = em.createQuery("select count(u) from User u", Long.class);
        long n = query.getSingleResult();
        return n;
    }
}
