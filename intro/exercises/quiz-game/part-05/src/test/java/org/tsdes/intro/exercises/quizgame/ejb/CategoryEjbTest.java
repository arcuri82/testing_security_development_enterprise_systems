package org.tsdes.intro.exercises.quizgame.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tsdes.intro.exercises.quizgame.entity.Category;
import org.tsdes.intro.exercises.quizgame.entity.SubCategory;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.tsdes.intro.exercises.quizgame")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CategoryEjb ctgEjb;

    @EJB
    private ResetEjb resetEjb;


    @Before
    public void init(){
        resetEjb.resetDatabase();
    }

    @Test
    public void testNoCategory(){

        List<Category> list = ctgEjb.getAllCategories(false);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateCategory(){

        String name = "testCreateCategory";

        Long id = ctgEjb.createCategory(name);
        assertNotNull(id);
    }

    @Test
    public void testGetCategory(){

        String name = "testGetCategory";

        long id = ctgEjb.createCategory(name);

        Category ctg = ctgEjb.getCategory(id, false);

        assertEquals(name, ctg.getName());
    }


    @Test
    public void testCreateSubCategory(){

        String ctgName = "ctg_testCreateSubCategory";
        long ctgId = ctgEjb.createCategory(ctgName);

        String subName = "sub_testCreateSubCategory";
        long subId = ctgEjb.createSubCategory(ctgId, subName);

        SubCategory sub = ctgEjb.getSubCategory(subId);
        assertEquals((Long) ctgId, sub.getParent().getId());
        assertEquals(subName, sub.getName());
    }

    @Test
    public void testGetAllCategories(){

        long a = ctgEjb.createCategory("a");
        long b = ctgEjb.createCategory("b");
        long c = ctgEjb.createCategory("c");

        ctgEjb.createSubCategory(a, "1");
        ctgEjb.createSubCategory(b, "2");
        ctgEjb.createSubCategory(c, "3");


        List<Category> categories = ctgEjb.getAllCategories(false);
        assertEquals(3, categories.size());

        Category first = categories.get(0);

        try {
            first.getSubCategories().size();
            fail();
        } catch (Exception e){
            //expected
        }

        categories = ctgEjb.getAllCategories(true);

        first = categories.get(0);

        assertEquals(1, first.getSubCategories().size());
    }

    @Test
    public void testCreateTwice(){

        String ctg = "Computer Science";

        ctgEjb.createCategory(ctg);

        try {
            ctgEjb.createCategory(ctg);
            fail();
        }catch (Exception e){
            //expected
        }
    }
}