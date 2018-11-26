package org.tsdes.intro.jee.jpa.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by arcuri82 on 25-Jan-17.
 */
public class NotStandardTest {

    private EntityManagerFactory emFactory;
    private EntityManager em;

    private ValidatorFactory valFactory;
    private Validator validator;

    @BeforeEach
    public void init() {
        emFactory = Persistence.createEntityManagerFactory("DB");
        em = emFactory.createEntityManager();

        valFactory = Validation.buildDefaultValidatorFactory();
        validator = valFactory.getValidator();
    }

    @AfterEach
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
        notStandard.setName("foo");
        notStandard.setEmail("foo@bar.com");
        notStandard.setAgeInYears(21);

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

    @Test
    public void testUrl(){
        NotStandard ns = getAValidInstance();
        ns.setHomePage("anInvalidHomePage");

        assertTrue(hasViolations(ns));
        assertFalse(persistInATransaction(ns));

        /*
            This is a valid URL
         */
        ns.setHomePage("https://github.com/arcuri82/testing_security_development_enterprise_systems");
        ns.setId(null);

        assertFalse(hasViolations(ns));
        assertTrue(persistInATransaction(ns));
    }

    @Test
    public void testRange() {
        NotStandard ns = getAValidInstance();

        //too small
        ns.setAgeInYears(-4);
        assertTrue(hasViolations(ns));
        assertFalse(persistInATransaction(ns));

        //too large
        ns.setAgeInYears(200);
        ns.setId(null);
        assertTrue(hasViolations(ns));
        assertFalse(persistInATransaction(ns));

        //fine
        ns.setAgeInYears(18);
        ns.setId(null);
        assertFalse(hasViolations(ns));
        assertTrue(persistInATransaction(ns));
    }
}