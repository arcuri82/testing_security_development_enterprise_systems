package org.tsdes.intro.jee.jpa.jpql;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.Assert.*;

public class UserTest {

    private EntityManagerFactory factory;
    private EntityManager em;


    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
        addDefaultData();
    }

    @After
    public void tearDown() {
        em.close();
        factory.close();
    }

    private void addDefaultData() {

        User a = new User();
        a.setName("A");
        a.getAddress().setCity("Oslo");
        a.getAddress().setCountry("Norway");

        User b = new User();
        b.setName("B");
        b.getAddress().setCity("Oslo");
        b.getAddress().setCountry("Norway");

        User b2 = new User();
        b2.setName("B");
        b2.getAddress().setCity("Bergen");
        b2.getAddress().setCountry("Norway");

        User c = new User();
        c.setName("C");
        c.getAddress().setCity("London");
        c.getAddress().setCountry("England");

        makeFriends(a, b);
        makeFriends(a, b2);
        makeFriends(a, c);

        makeFriends(b, b2);

        assertTrue(persistInATransaction(a, b, b2, c));
    }

    private void makeFriends(User x, User y) {
        x.getFriends().add(y);
        y.getFriends().add(x);
    }

    private boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (Object o : obj) {
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

    @Test
    public void testGetAll() {

        TypedQuery<User> query = em.createNamedQuery(User.GET_ALL, User.class);

        //being a read operation, no need to have an explicit transaction here
        List<User> users = query.getResultList();

        assertEquals(4, users.size());
    }

    @Test
    public void testGetAllInNorway() {

        TypedQuery<User> query = em.createNamedQuery(User.GET_ALL_IN_NORWAY, User.class);
        List<User> users = query.getResultList();

        assertEquals(3, users.size());
    }

    @Test
    public void testGetAllWithOnTheFlyQuery() {

        //you can create queries on the fly. but if a query is used in a lot of different
        //places, it might be best to use a named one
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<User> users = query.getResultList();

        assertEquals(4, users.size());
    }

    @Test
    public void testCaseWhen() {

        // can use "CASE WHEN THEN ELSE" to return different values based on some checks
        TypedQuery<String> query = em.createQuery(
                "select CASE u.address.city WHEN 'Oslo' THEN 'yes' ELSE 'no' END " +
                        "from User u", String.class);

        List<String> osloCounters = query.getResultList();
        assertEquals(4, osloCounters.size());

        assertEquals(2, osloCounters.stream().filter(s -> s.equals("yes")).count());
        assertEquals(2, osloCounters.stream().filter(s -> s.equals("no")).count());
    }

    @Test
    public void testNew() {

        /*
            can create new objects based on the data in the database.
            Note: this is particularly useful for efficiency: let's say you
            have an Entity with a lot of data (ie fields), but you just
            need a small subset: by creating this kind of object on the fly,
            only the needed data is read from database, and not the whole Entity
         */
        TypedQuery<Message> query = em.createQuery(
                "select NEW " + Message.class.getName() + "(u.name) from User u", Message.class);

        List<Message> messages = query.getResultList();

        assertEquals(4, messages.size());
        assertTrue(messages.stream().map(Message::getText).anyMatch(t -> t.equals("A")));
        assertTrue(messages.stream().map(Message::getText).anyMatch(t -> t.equals("B")));
        assertTrue(messages.stream().map(Message::getText).anyMatch(t -> t.equals("C")));
    }


    @Test
    public void testDistinct() {

        TypedQuery<String> query = em.createQuery(
                "select distinct u.name from User u", String.class);

        List<String> messages = query.getResultList();

        //recall: 2 users have same name (ie B)
        assertEquals(3, messages.size());
    }


    @Test
    public void testAvg() {

        //how many friends on average?  3+2+2+1 / 4 = 2
        TypedQuery<Double> queryAvg = em.createQuery(
                "select avg(u.friends.size) from User u", Double.class);

        double avg = queryAvg.getSingleResult();
        assertEquals(2.0, avg, 0.001);

        //other operators are for example: count, max, min, sum

        TypedQuery<Long> querySum = em.createQuery(
                "select sum(u.friends.size) from User u", Long.class);

        long sum = querySum.getSingleResult();
        assertEquals(8L, sum);
    }

    @Test
    public void testBindingParameters() {

        assertEquals(3, findUsers("Norway").size());
        assertEquals(2, findUsers("Norway", "Oslo").size());
    }

    private List<User> findUsers(String country) {

        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.address.country = :country", User.class); //note the ":"
        query.setParameter("country", country);

        return query.getResultList();
    }

    private List<User> findUsers(String country, String city) {

        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.address.country = ?1 and u.address.city = ?2",
                User.class);
        query.setParameter(1, country); //yep, it starts from 1, and not 0...
        query.setParameter(2, city);

        return query.getResultList();
    }


    @Test
    public void testInjection() {

        String param = "Norway' or '1' = '1"; // this results in a tautology

        /*
            here we actually read the whole table, because:

            where u.address.country = '" + country + "'
            ->
            where u.address.country = 'Norway' or '1' = '1'
            ->
            is always true
         */
        assertEquals(4, findUsers_IN_A_VERY_WRONG_WAY(param).size());

        // this fails to retrieve any data, as one would hope
        assertEquals(0, findUsers(param).size());
    }


    private List<User> findUsers_IN_A_VERY_WRONG_WAY(String country) {

        /*
            NEVER EVER write something like this, ie concatenating a JPQL/SQL query with a + on
            an input parameter... reasons is that SQL injections can compromise the database
         */

        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.address.country = '" + country + "'",
                User.class);
        return query.getResultList();
    }


    @Test
    public void testSubquery() {

        //find all users that have at least a friend in a different country
        TypedQuery<User> query = em.createQuery(
                "select u from User u where " +
                        "(select count(f) from User f where (u member of f.friends) and (u.address.country != f.address.country)) " +
                        "> 0",
                User.class);


        List<User> users = query.getResultList();
        assertEquals(2, users.size());
        assertTrue(users.stream().map(User::getName).anyMatch(n -> "A".equals(n))); //note the "Yoda-style" to prevent NPE
        assertTrue(users.stream().map(User::getName).anyMatch(n -> "C".equals(n)));
    }

    @Test
    public void testCriteriaBuilder() {

        /*
            You can use CriteriaBuilder to avoid writing strings, which is more type-safe.
            However, is far more verbose and arguably more difficult to read and understand.
            Considering that IntelliJ can validate the syntax of the JPQL strings,
            I can't really recommend the use of CriteriaBuilder
         */

        //Query query =     em.createQuery("select u from User u where u.address.country = 'Norway'");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> q = builder.createQuery(User.class);
        Root<User> u = q.from(User.class);
        q.select(u).where(builder.equal(u.get("address").get("country"), "Norway"));

        TypedQuery<User> query = em.createQuery(q);
        assertEquals(3, query.getResultList().size());
    }


    @Test
    public void testFindUsersWith_SQL_insteadOf_JPQL() {

        //Query query =     em.createQuery("select u from User u where u.address.country = 'Norway'");
        Query query = em.createNativeQuery("SELECT * FROM User WHERE country = 'Norway'", User.class);

        assertEquals(3, query.getResultList().size());

        /*
            There are a few cases in which you might want to use SQL directly instead of JPQL:
            - need to use special features of the DB not supported by JPA/Hibernate
            - queries generated by JPA/Hibernate are not efficient
            - JPA/Hibernate are wrong (ie not doing what you would expect)

            Recall:
            - SQL works directly on the actual tables in the DB (and there is no Address table there)
            - JPQL works at the @Entity level (so User entity has no country, and one has to access it
             through the embedded object u.address)
         */
    }


    @Test
    public void testFindUsersWithJOOQ() {

        //Query query =     em.createQuery("select u from User u where u.address.country = 'Norway'");
        //Query query = em.createNativeQuery("select * from User where country = 'Norway'");

        DSLContext create = DSL.using(SQLDialect.H2);
        String sql = create
                .select()
                .from(table("User"))
                .where(field("country").eq("Norway"))
                .getSQL(ParamType.INLINED);

        Query query = em.createNativeQuery(sql, User.class);

        List<User> results = query.getResultList();

        assertEquals(3, results.size());

        /*
           JOOQ is a popular, easy to use DSL for writing SQL (not JPQL).
           Besides type-safety and IDE code-completion, one HUGE benefit
           is that the SQL is targeted for the specific dialect of the
           target DB.
         */
    }

    @Test
    public void testBulkDeleteAll() {
        TypedQuery<User> all = em.createNamedQuery(User.GET_ALL, User.class);
        assertEquals(4, all.getResultList().size());

        //useful when needing to delete/update many entries at the same time
        Query delete = em.createQuery("delete from User u");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //as it modifies the DB, we need to explicitly wrap it inside a transaction
            delete.executeUpdate();
        } catch (Exception e) {
            tx.rollback();
            fail();
        }

        assertEquals(0, all.getResultList().size()); //all should had been deleted
    }

    @Test
    public void testBulkDelete() {

        TypedQuery<User> all = em.createNamedQuery(User.GET_ALL, User.class);
        assertEquals(4, all.getResultList().size());

        String country = "Norway";

        //useful when needing to delete/update many entries at the same time
        Query delete = em.createQuery("delete from User u where u.address.country = :country ");
        delete.setParameter("country", country);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //as it modifies the DB, we need to explicitly wrap it inside a transaction
            delete.executeUpdate();
            fail();
        } catch (Exception e) {
            tx.rollback();
            /*
                expected, because it would leave the DB in a inconsistent state, as there would
                be some non-Norwegian users that have at least one Norwegian friend, and that link
                would be broken if bulk delete gets executed
             */
        }

        assertEquals(4, all.getResultList().size()); //rollback, nothing should had been deleted so far

        /*
            to bulk delete the elements in the 'friends' list,
            can't use JPQL (as far as I know...) but can always use native SQL.

            Note: the relation table linking a manyToMany relation between X and Y will be called X_Y, which
            is User_User in our case

            Note2: there might be different ways to efficiently do this query, which might also depends on
             the actual database dialect

            Note3: "select 1" (ie just return '1' value for each row) is only done for efficiency, as in the EXISTS
             we just want to check if the return of the SELECT is non-empty
         */
        Query deletedRelation = em.createNativeQuery(
                "DELETE FROM User_User K WHERE EXISTS(SELECT 1 FROM USER w WHERE K.friends_id=w.id AND w.country=?1) ");
        deletedRelation.setParameter(1, country);


        tx = em.getTransaction();
        tx.begin();
        try {

            deletedRelation.executeUpdate();
            delete.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail();
        }

        assertEquals(1, all.getResultList().size()); // the 3 Norwegian users should had been deleted now
    }

    @Test
    public void testOderBy() {

        /*
            Recall:
            A: 3
            B: 2
            B: 2
            C: 1
         */

        TypedQuery<User> query = em.createQuery("select u from User u order by u.friends.size ASC", User.class);
        query.setMaxResults(2); // return at most 2 values

        List<User> users = query.getResultList();
        assertEquals(2, users.size());

        //in this case, "users" is ordered
        assertEquals("C", users.get(0).getName());
        assertEquals("B", users.get(1).getName());
    }
}