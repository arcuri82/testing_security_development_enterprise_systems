package org.tsdes.jee.exercises.mynews.backend.ejb;


import org.apache.commons.codec.digest.DigestUtils;
import org.tsdes.jee.exercises.mynews.backend.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;


@Stateless
public class UserEJB implements Serializable{

    @PersistenceContext
    private EntityManager em;

    public UserEJB(){
    }


    public long computeKarma(@NotNull String userId){

        User user = getUser(userId);
        if(user == null){
            throw new IllegalArgumentException("User "+ userId +" does not exist");
        }

        int sum = 0;
        Query query;

        //note: "from Post" will also pick up the Comment
        query = em.createQuery("select size(p.votesFor) from Post p where p.author.userId = ?1 ");
        query.setParameter(1, userId);
        sum += (Integer) query.getSingleResult();

        query = em.createQuery("select size(p.votesAgainst) from Post p where p.author.userId = ?1 ");
        query.setParameter(1, userId);
        sum -= (Integer) query.getSingleResult();


        Query moderated = em.createQuery("select count(c) from Comment c where c.author.userId = ?1 and c.moderated = true");
        moderated.setParameter(1, userId);

        sum += (int) ((-10) * (Long) moderated.getSingleResult());

        return sum;
    }


    /**
     *
     * @return {@code false} if for any reason it was not possible to create the user
     */
    public boolean createUser(String userId, String password, String firstName, String middleName, String lastName) {
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
        user.setRegistrationTime(new Date());

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
            computeHash(password, getSalt());
            return false;
        }

        String hash = computeHash(password, userDetails.getSalt());

        return hash.equals(userDetails.getHash());
    }


    public User getUser(String userId){
        return em.find(User.class, userId);
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
