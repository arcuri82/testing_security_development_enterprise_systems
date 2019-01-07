package org.tsdes.intro.jee.jpa.relationshipsql;

import org.junit.jupiter.api.Test;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.bidirectional.X_1_to_m_bi;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.bidirectional.Y_1_to_m_bi;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.joincolumn.X_1_to_m_join;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.joincolumn.Y_1_to_m_join;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.manytoone.X_1_to_m_mt1;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.manytoone.Y_1_to_m_mt1;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.unidirectional.X_1_to_m_uni;
import org.tsdes.intro.jee.jpa.relationshipsql.onetomany.unidirectional.Y_1_to_m_uni;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
public class OneToManyTest extends TestBase {


    @Test
    public void testUnidirectional(){

        X_1_to_m_uni x = new X_1_to_m_uni();
        Y_1_to_m_uni y0 = new Y_1_to_m_uni();
        Y_1_to_m_uni y1 = new Y_1_to_m_uni();

        x.getYs().add(y0);
        x.getYs().add(y1);

        /*
            Here, we have links from X to Y. But, to achieve it,
            we need an intermediary table (which we do not directly
            map with an @Entity)
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }


    @Test
    public void testManyToOne(){

        X_1_to_m_mt1 x = new X_1_to_m_mt1();
        Y_1_to_m_mt1 y0 = new Y_1_to_m_mt1();
        Y_1_to_m_mt1 y1 = new Y_1_to_m_mt1();

        y0.setX(x);
        y1.setX(x);

        /*
            Conceptually, M-to-1 is the same as 1-to-M,
            but we just have links back from Y to X.
            We do not need an intermediate table, as can just
            use FKs in Y
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }


    @Test
    public void testBidirectional(){

        X_1_to_m_bi x = new X_1_to_m_bi();
        Y_1_to_m_bi y0 = new Y_1_to_m_bi();
        Y_1_to_m_bi y1 = new Y_1_to_m_bi();

        x.getYs().add(y0);
        x.getYs().add(y1);
        y0.setX(x);
        y1.setX(x);

        /*
            The most interesting thing year is that the SQL of database is exactly
            the same as the @ManyToOne case. We just need a FK from Y to X, and no
            need for intermediary table.
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }


    @Test
    public void testJoin(){

        X_1_to_m_join x = new X_1_to_m_join();
        Y_1_to_m_join y0 = new Y_1_to_m_join();
        Y_1_to_m_join y1 = new Y_1_to_m_join();

        x.getYs().add(y0);
        x.getYs().add(y1);

        /*
            As for the other cases, the SQL database tables are the same, just
            a FK from Y to X.
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }
}
