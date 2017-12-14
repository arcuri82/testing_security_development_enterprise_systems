package org.tsdes.intro.jee.jpa.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 25-Jan-17.
 */
public class NotStandardTest {

    private EntityManagerFactory emFactory;
    private EntityManager em;

    private ValidatorFactory valFactory;
    private Validator validator;

    @Before
    public void init() {
        emFactory = Persistence.createEntityManagerFactory("DB");
        em = emFactory.createEntityManager();

        valFactory = Validation.buildDefaultValidatorFactory();
        validator = valFactory.getValidator();
    }

    @After
    public void tearDown() {
        em.close();
        emFactory.close();
        valFactory.close();
    }

    private boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }

    private NotStandard getAValidInstance(){

        NotStandard notStandard = new NotStandard();
        notStandard.setName("     ");
        notStandard.setSurname("foo");
        notStandard.setDateOfBirth(ZonedDateTime.now().minusHours(1));
        notStandard.setEmail("foo@bar.com");

        return notStandard;
    }

    private <T> boolean hasViolations(T obj){
        Set<ConstraintViolation<T>> violations = validator.validate(obj);

        for(ConstraintViolation<T> cv : violations){
            System.out.println("VIOLATION: "+cv.toString());
        }

        return violations.size() > 0;
    }


    @Test
    public void testValid(){

        NotStandard ns = getAValidInstance();

        //no violation of the constraints
        assertFalse(hasViolations(ns));

        //can persist to database
        assertTrue(persistInATransaction(ns));
    }

    @Test
    public void testBlankSurname(){

        NotStandard ns = getAValidInstance();
        ns.setSurname("    ");

        assertTrue(hasViolations(ns));
        assertFalse(persistInATransaction(ns));
    }

    @Test
    public void testShortSurname(){

        NotStandard ns = getAValidInstance();
        ns.setSurname("   a  ");

        /*
            This does not fail the constrain: it is
            not blank (there is a "a") and at least two
            characters (empty spaces)
         */
        assertFalse(hasViolations(ns));
        assertTrue(persistInATransaction(ns));
    }

    @Test
    public void testBornInTheFuture(){

        NotStandard ns = getAValidInstance();
        ns.setDateOfBirth(ZonedDateTime.now().plusYears(1));

        assertTrue(hasViolations(ns));
        assertFalse(persistInATransaction(ns));
    }

    @Test
    public void testEmail(){
        NotStandard ns = getAValidInstance();
        ns.setEmail("anInvalidEmail");

        assertTrue(hasViolations(ns));
        assertFalse(persistInATransaction(ns));

        /*
            This is actually a valid email, as top level
            domain (eg .com and .no) are not required
         */
        ns.setEmail("thisIsActually@valid");
        ns.setId(null);

        assertFalse(hasViolations(ns));
        assertTrue(persistInATransaction(ns));
    }
}