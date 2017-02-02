package org.tsdes.jee.jpa.outerjoin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 02-Feb-17.
 */
public class OuterJoinTest {

    private EntityManagerFactory factory;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @After
    public void tearDown() {
        factory.close();
    }

    private boolean persistInATransaction(Object... obj) {
        EntityManager em = factory.createEntityManager();
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
        } finally {
            em.close();
        }

        return true;
    }


    private long initData(){

        A a = new A();
        B b0 = new B();
        B b1 = new B();
        a.getListB().add(b0);
        a.getListB().add(b1);

        C c0 = new C();
        C c1 = new C();
        C c2 = new C();
        C c3 = new C();

        b0.getListC().add(c0);
        b1.getListC().add(c1);
        b1.getListC().add(c2);
        b1.getListC().add(c3);

        assertTrue(persistInATransaction(a, b0, b1, c0, c1, c2, c3));

        return a.getId();
    }

    @Test
    public void test(){

        long id = initData();

        EntityManager em = factory.createEntityManager();
        Query query = em.createQuery("select a from A a where a.id="+id);

        System.out.println("\n* Going to execute JPQL query");
        A a = (A) query.getSingleResult();
        /*
            Hibernate: select a0_.id as id1_0_ from A a0_ where a0_.id=1
            Hibernate: select listb0_.A_id as A_id1_1_0_, listb0_.listB_id as listB_id2_1_0_, b1_.id as id1_2_1_ from A_B listb0_ inner join B b1_ on listb0_.listB_id=b1_.id where listb0_.A_id=?
            Hibernate: select listc0_.B_id as B_id1_3_0_, listc0_.listC_id as listC_id2_3_0_, c1_.id as id1_4_1_ from B_C listc0_ inner join C c1_ on listc0_.listC_id=c1_.id where listc0_.B_id=?
            Hibernate: select listc0_.B_id as B_id1_3_0_, listc0_.listC_id as listC_id2_3_0_, c1_.id as id1_4_1_ from B_C listc0_ inner join C c1_ on listc0_.listC_id=c1_.id where listc0_.B_id=?
         */

        assertEquals(2, a.getListB().size());

        em.clear();

        System.out.println("\n* Going to execute 'em.find'");
        A aFromFind = em.find(A.class, id);
        //Hibernate: select a0_.id as id1_0_0_, listb1_.A_id as A_id1_1_1_, b2_.id as listB_id2_1_1_, b2_.id as id1_2_2_, listc3_.B_id as B_id1_3_3_, c4_.id as listC_id2_3_3_, c4_.id as id1_4_4_ from A a0_ left outer join A_B listb1_ on a0_.id=listb1_.A_id left outer join B b2_ on listb1_.listB_id=b2_.id left outer join B_C listc3_ on b2_.id=listc3_.B_id left outer join C c4_ on listc3_.listC_id=c4_.id where a0_.id=?

        assertEquals(4, aFromFind.getListB().size());

        em.close();
    }
}