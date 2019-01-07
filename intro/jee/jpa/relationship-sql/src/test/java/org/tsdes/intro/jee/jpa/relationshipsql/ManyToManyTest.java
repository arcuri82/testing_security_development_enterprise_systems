package org.tsdes.intro.jee.jpa.relationshipsql;

import org.junit.jupiter.api.Test;
import org.tsdes.intro.jee.jpa.relationshipsql.manytomany.bidirectional.X_m_to_m_bi;
import org.tsdes.intro.jee.jpa.relationshipsql.manytomany.bidirectional.Y_m_to_m_bi;
import org.tsdes.intro.jee.jpa.relationshipsql.manytomany.unidirectional.X_m_to_m_uni;
import org.tsdes.intro.jee.jpa.relationshipsql.manytomany.unidirectional.Y_m_to_m_uni;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
public class ManyToManyTest extends TestBase {

    @Test
    public void testUnidirectional(){

        X_m_to_m_uni x = new X_m_to_m_uni();
        Y_m_to_m_uni y0 = new Y_m_to_m_uni();
        Y_m_to_m_uni y1 = new Y_m_to_m_uni();

        x.getYs().add(y0);
        x.getYs().add(y1);

        assertTrue(persistInATransaction(x, y0, y1));
    }

    @Test
    public void testBidirectional(){

        X_m_to_m_bi x0 = new X_m_to_m_bi();
        X_m_to_m_bi x1 = new X_m_to_m_bi();
        Y_m_to_m_bi y0 = new Y_m_to_m_bi();
        Y_m_to_m_bi y1 = new Y_m_to_m_bi();

        x0.getYs().add(y0);
        x0.getYs().add(y1);
        x1.getYs().add(y1);

        y0.getXs().add(x0);
        y1.getXs().add(x0);
        y1.getXs().add(x1);

        /*
            The SQL tables in the database are the same as in the
            unidirectional case
         */
        assertTrue(persistInATransaction(x0, x1, y0, y1));
    }
}
