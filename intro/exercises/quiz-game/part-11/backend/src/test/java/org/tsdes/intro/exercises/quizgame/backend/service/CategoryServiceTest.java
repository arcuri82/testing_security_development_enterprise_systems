package org.tsdes.intro.exercises.quizgame.backend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.exercises.quizgame.backend.StubApplication;
import org.tsdes.intro.exercises.quizgame.backend.entity.Category;
import org.tsdes.intro.exercises.quizgame.backend.entity.SubCategory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CategoryServiceTest extends ServiceTestBase{

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testNoCategory(){

        List<Category> list = categoryService.getAllCategories(false);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateCategory(){

        String name = "Computer Science";

        long id = categoryService.createCategory(name);

        Category c = categoryService.getCategory(id, false);

        assertNotNull(c);
        assertEquals(1, categoryService.getAllCategories(false).size());
        assertEquals(name, c.getName());
    }


    @Test
    public void testCreateSubCategory(){

        String ctg = "Computer Science";
        long ctgId = categoryService.createCategory(ctg);

        String subCtg = "Java Enterprise Edition";
        long subCtgId = categoryService.createSubCategory(ctgId, subCtg);

        SubCategory s = categoryService.getSubCategory(subCtgId);
        assertNotNull(s);
        assertEquals(subCtg, s.getName());

        Category c = categoryService.getCategory(ctgId, true);
        assertNotNull(c);
        assertEquals(1, c.getSubCategories().size());
        assertEquals(subCtg, c.getSubCategories().get(0).getName());
    }

    @Test
    public void testCreateTwice(){

        String ctg = "Computer Science";

        categoryService.createCategory(ctg);

        try {
            categoryService.createCategory(ctg);
            fail();
        }catch (Exception e){
            //expected
        }
    }
}