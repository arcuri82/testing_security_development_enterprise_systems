package org.tsdes.jee.exercises.quizgame.backend.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 06-Dec-17.
 */
public class CategoryTest extends EntityTestBase{


    @Test
    public void testCreateCategory(){

        Category ctg = new Category();
        ctg.setName("Foo");

        assertTrue(persistInATransaction(ctg));
    }
}