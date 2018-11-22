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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

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

    private User getAValidUser(){
        User user = new User();
        user.setName("Foo");
        user.setMiddleName(null);
        user.setSurname("Bar");
        user.setEmail("foobar@gmail.com");

        //converting to/from times in Java 7 and 8 is not so nice...
        user.setDateOfBirth(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        user.setDateOfRegistration(Date.from(LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return user;
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

        User user = getAValidUser();

        //no violation of the constraints
        assertFalse(hasViolations(user));

        //can persist to database
        assertTrue(persistInATransaction(user));
    }

    @Test
    public void testNoName(){

        User user = getAValidUser();
        user.setName(null);

        assertTrue(hasViolations(user));

        //persist to database should fail
        assertFalse(persistInATransaction(user));
    }


    @Test
    public void testShortName(){

        User user = getAValidUser();
        user.setName("a");

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testLongName(){

        User user = getAValidUser();
        user.setName(new String(new char[1_000]));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testBlankName(){

        User user = getAValidUser();
        user.setName("      ");

        /*
            There is no constraint on the Entity that a name should
            not be blank, ie all empty spaces.
            Would need a regular expression avoiding spaces, or
            a custom one
         */
        assertFalse(hasViolations(user));
        assertTrue(persistInATransaction(user));
    }



    @Test
    public void testMiddleName(){

        User user = getAValidUser();

        //null is OK
        user.setMiddleName(null);
        assertFalse(hasViolations(user));

        //empty is OK
        user.setMiddleName("");
        assertFalse(hasViolations(user));

        //but too long is a problem
        user.setMiddleName(new String(new char[1_000]));
        assertTrue(hasViolations(user));
    }

    @Test
    public void testRegistrationInTheFuture(){
        User user = getAValidUser();
        user.setDateOfRegistration(Date.from(LocalDate.of(2116, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testTooYoung(){
        User user = getAValidUser();
        user.setDateOfBirth(Date.from(LocalDate.of(2014, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));

        /*
            note: this test will fail when I ll teach JEE in 2032... however, not sure
            if JEE (or Java) will still exist...
         */
    }

    @Test
    public void testRegisteredBeforeBeingBorn(){
        User user = getAValidUser();
        user.setDateOfBirth(Date.from(LocalDate.of(1980, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        user.setDateOfRegistration(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }


    @Test
    public void testEmail(){
        User user = getAValidUser();
        user.setEmail("anInvalidEmail");

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));

        user.setEmail("stillThisIs@nInvalidEmail");
        user.setId(null);

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testUnique(){

        String email = "unique@foo.com";

        User a = getAValidUser();
        a.setEmail(email);
        assertTrue(persistInATransaction(a));

        User b = getAValidUser();
        b.setEmail(email+".uk");
        assertTrue(persistInATransaction(b));

        User c = getAValidUser();
        c.setEmail(email); //same email
        assertFalse(persistInATransaction(c));
    }
}