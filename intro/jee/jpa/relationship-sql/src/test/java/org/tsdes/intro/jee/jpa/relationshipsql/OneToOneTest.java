package org.tsdes.intro.jee.jpa.relationshipsql;

import org.junit.jupiter.api.Test;
import org.tsdes.intro.jee.jpa.relationshipsql.onetoone.bidirectional.X_1_to_1_bi;
import org.tsdes.intro.jee.jpa.relationshipsql.onetoone.bidirectional.Y_1_to_1_bi;
import org.tsdes.intro.jee.jpa.relationshipsql.onetoone.independent.X_1_to_1_ind;
import org.tsdes.intro.jee.jpa.relationshipsql.onetoone.independent.Y_1_to_1_ind;
import org.tsdes.intro.jee.jpa.relationshipsql.onetoone.unidirectional.X_1_to_1_uni;
import org.tsdes.intro.jee.jpa.relationshipsql.onetoone.unidirectional.Y_1_to_1_uni;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by arcuri82 on 07-Jan-19.
 */
public class OneToOneTest extends TestBase{

    @Test
    public void testUnidirectional(){

        X_1_to_1_uni x = new X_1_to_1_uni();
        Y_1_to_1_uni y = new Y_1_to_1_uni();

        x.setY(y);

        assertTrue(persistInATransaction(x, y));
    }

    @Test
    public void testBidirectional(){

        X_1_to_1_bi x = new X_1_to_1_bi();
        Y_1_to_1_bi y = new Y_1_to_1_bi();

        x.setY(y);
        y.setX(x);

        assertTrue(persistInATransaction(x, y));
    }

    @Test
    public  void testBidirectionalWrongFKs(){

        X_1_to_1_bi x = new X_1_to_1_bi();
        Y_1_to_1_bi y = new Y_1_to_1_bi();
        x.setY(y);
        y.setX(x);

        X_1_to_1_bi k = new X_1_to_1_bi();
        k.setY(y);

        /*
            Conceptually, this is wrong, ie K points to Y, but Y does not
            point back to the same K (and rather to X).
            But this does not violates the foreign constraints in the database
         */
        assertTrue(persistInATransaction(x, y, k));
    }

    @Test
    public  void testIndependent(){

        X_1_to_1_ind x = new X_1_to_1_ind();
        Y_1_to_1_ind y = new Y_1_to_1_ind();
        x.setY(y);
        y.setX(x);

        X_1_to_1_ind k = new X_1_to_1_ind();
        k.setY(y);

        /*
            This is fine, as expected. However, notice that the actual tables
            in SQL database are different than in the Bidirectional case, ie
            extra FK in table for Y
         */
        assertTrue(persistInATransaction(x, y, k));
    }
}
