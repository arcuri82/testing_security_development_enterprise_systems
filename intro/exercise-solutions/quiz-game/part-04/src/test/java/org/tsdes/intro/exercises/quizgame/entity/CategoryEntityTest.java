package org.tsdes.intro.exercises.quizgame.entity;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryEntityTest extends EntityTestBase {

    @Test
    public void testTooLongName(){

        String name = new String(new char[150]);

        Category category = new Category();
        category.setName(name);

        assertFalse(persistInATransaction(category));

        category.setId(null);
        category.setName("foo");

        assertTrue(persistInATransaction(category));
    }

    @Test
    public void testUniqueName(){

        String name = "bar";

        Category category = new Category();
        category.setName(name);

        assertTrue(persistInATransaction(category));

        Category another = new Category();
        another.setName(name);

        assertFalse(persistInATransaction(another));
    }
}