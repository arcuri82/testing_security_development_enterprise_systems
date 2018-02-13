package org.tsdes.intro.spring.security.manual.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tsdes.intro.spring.security.manual.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.security.SecureRandom;


@Service
@Transactional
public class UserService {

    @PersistenceContext
    private EntityManager em;

    private static final String PEPPER = "some strings, that needs to be stored outside the database";

    private static final int ITERATIONS = 10_000;

    private static final char SEPARATOR = '$';

    private static final char SEPARATOR_REPLACEMENT = '!';

    /**
     * @return {@code false} if any initial validation fails.
     *         Note: this method can still throw an exception if the transaction fails
     */
    public boolean createUser(String userId, String password) {
        if (userId == null) {
            return false;
        }

        User userDetails = getUser(userId);
        if (userDetails != null) {
            //a user with same id already exists
            return false;
        }

        userDetails = new User();
        userDetails.setUserId(userId);

        //create a "strong" random string of at least 128 bits, needed for the "salt"
        String salt = getRandomSalt();

        String hash = computeSHA256(password, ITERATIONS, salt, PEPPER);

        /*
            Note: the paper is not stored in the string in the database.
            However, the ITERATIONS and the algorithm used need to be stored,
            because we might change them in the future (eg with something more
            expensive, as hardware will get faster)
         */
        String hashedPassword = "SHA-256"+SEPARATOR+ITERATIONS+SEPARATOR+salt+SEPARATOR+hash;

        userDetails.setPassword(hashedPassword);

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
        if (userId == null) {
            return false;
        }

        User user = getUser(userId);
        if (user == null) {

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
            computeSHA256(password, ITERATIONS, getRandomSalt(), PEPPER);

            return false;
        }

        String storedHashedPassword = user.getPassword();
        String[] tokens = storedHashedPassword.split("\\"+SEPARATOR);

        //NOTE: if we change algorithm, we would need to check it as well

        int iterations = Integer.parseInt(tokens[1]);
        String salt = tokens[2];
        String hash = tokens[3];

        String x = computeSHA256(password, iterations, salt, PEPPER);

        //check if the computed hash is equal to what stored in the DB
        return  x.equals(hash);
    }


    public User getUser(String userId){
        return em.find(User.class, userId);
    }


    //------------------------------------------------------------------------


    protected String computeSHA256(String password, int iterations, String salt, String pepper){

         /*
            Given an hash function "f", we have

            f(password) = hash

            the main point is that, even if one knows the details of "f" and has the hash,
            then it is extremely difficult to derive the input password, ie find a function "g"
            such that

            g(hash) = password
         */

        String hash = password + salt + pepper;

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

            Note: a hacker accessing the database can read the "salt", but would
            not be able to read the "pepper".

            Note: DigestUtils from commons-codec library is just an utility to simplify
            the usage of Java API own MessageDigest class
         */

        for(int i=0; i<iterations; i++) {
            hash = DigestUtils.sha256Hex(hash + salt + pepper);
        }

        return hash;
    }

    protected String getRandomSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        //we should avoid the separator being part of the random string
        salt = salt.replace(SEPARATOR, SEPARATOR_REPLACEMENT);
        return salt;
    }
}
