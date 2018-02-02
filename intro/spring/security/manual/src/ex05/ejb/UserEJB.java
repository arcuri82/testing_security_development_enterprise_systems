package org.tsdes.intro.jee.jsf.examples.ex05.ejb;


import org.apache.commons.codec.digest.DigestUtils;
import org.tsdes.intro.jee.jsf.examples.ex05.entity.UserDetails;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *  A good explanation/tutorial on how to handle passwords can be found at:
 *  <p>
 *  https://wblinks.com/notes/storing-passwords-the-wrong-better-and-even-better-way/
 *  <p>
 *  Note: this is independent from Java/JEE
 */
@Stateless
public class UserEJB implements Serializable{

    @PersistenceContext()
    private EntityManager em;

    public UserEJB(){
    }

    /**
     *
     * @param userId
     * @param password
     * @return {@code false} if for any reason it was not possible to create the user
     */
    public boolean createUser(String userId, String password) {
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        UserDetails userDetails = getUser(userId);
        if (userDetails != null) {
            //a user with same id already exists
            return false;
        }

        userDetails = new UserDetails();
        userDetails.setUserId(userId);

        //create a "strong" random string of at least 128 bits, needed for the "salt"
        String salt = getSalt();
        userDetails.setSalt(salt);

        String hash = computeHash(password, salt);
        userDetails.setHash(hash);

        em.persist(userDetails);

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

        UserDetails userDetails = getUser(userId);
        if (userDetails == null) {

            /*
                Why computing an hash with a random salt without storing it anywhere???
                Well, if you are puzzled, then welcome to the wonderful world of security!
                Point is, if you do not compute it, then the "login" method will be
                faster when there is no user with the given userId (hash computation is
                usually quite expensive).
                An attacker can monitor the response time of each login attempt, and so
                determine when a userId already exists or not...

                That is also a reason why, when you fail to login in a website, usually
                it does not tell you if it was the userId or the password that was wrong...
             */
            computeHash(password, getSalt());

            return false;
        }

        String hash = computeHash(password, userDetails.getSalt());

        //check if the computed hash is equal to what stored in the DB
        return  hash.equals(userDetails.getHash());
    }


    public UserDetails getUser(String userId){
        return em.find(UserDetails.class, userId);
    }


    //------------------------------------------------------------------------


    @NotNull
    protected String computeHash(String password, String salt){

         /*
            Given an hash function "f", we have

            f(password) = hash

            the main point is that, even if one knows the details of "f" and has the hash,
            then it is extremely difficult to derive the input password, ie find a function "g"
            such that

            g(hash) = password
         */

        String combined = password + salt;

        /*
            The password is combined with a "salt" to avoid:

            1) two users with same password having same hash. Even if one password gets
               compromised, an attacker would have no way to know if any other
               user has the same password
            2) make nearly impossible a brute force attack based on
               "rainbow tables", ie pre-computed values of all hashes from
               password strings up to length N.
               This is because now the hashed string
               will be at least the length of the salt (eg 26) regardless of
               the length of the password.

            Note: DigestUtils from commons-codec library is just an utility to simplify
            the usage of Java API own MessageDigest class
         */

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
