package org.tsdes.jee.exercises.quizgame.backend.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.jee.exercises.quizgame.backend.entity.Category;
import org.tsdes.jee.exercises.quizgame.backend.entity.SubCategory;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 06-Dec-17.
 */
@RunWith(Arquillian.class)
public class CategoryEjbTest extends EjbTestBase{

    @Test
    public void testNoCategory(){

        List<Category> list = categoryEjb.getAllCategories(false);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateCategory(){

        String name = "Computer Science";

        long id = categoryEjb.createCategory(name);

        Category c = categoryEjb.getCategory(id, false);

        assertNotNull(c);
        assertEquals(1, categoryEjb.getAllCategories(false).size());
        assertEquals(name, c.getName());
    }


    @Test
    public void testCreateSubCategory(){

        String ctg = "Computer Science";
        long ctgId = categoryEjb.createCategory(ctg);

        String subCtg = "Java Enterprise Edition";
        long subCtgId = categoryEjb.createSubCategory(ctgId, subCtg);

        SubCategory s = categoryEjb.getSubCategory(subCtgId);
        assertNotNull(s);
        assertEquals(subCtg, s.getName());

        Category c = categoryEjb.getCategory(ctgId, true);
        assertNotNull(c);
        assertEquals(1, c.getSubCategories().size());
        assertEquals(subCtg, c.getSubCategories().get(0).getName());
    }
}