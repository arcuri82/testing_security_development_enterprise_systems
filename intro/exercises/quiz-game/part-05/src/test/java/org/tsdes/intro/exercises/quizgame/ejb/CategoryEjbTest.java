package org.tsdes.intro.exercises.quizgame.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
    private CategoryEjb ejb;


    @Test
    public void testCreateCategory(){

        String name = "testCreateCategory";

        Long id = ejb.createCategory(name);
        assertNotNull(id);
    }

    @Test
    public void testGetCategory(){

        String name = "testGetCategory";

        long id = ejb.createCategory(name);

        Category ctg = ejb.getCategory(id, false);

        assertEquals(name, ctg.getName());
    }


    @Test
    public void testCreateSubCategory(){

        String ctgName = "ctg_testCreateSubCategory";
        long ctgId = ejb.createCategory(ctgName);

        String subName = "sub_testCreateSubCategory";
        long subId = ejb.createSubCategory(ctgId, subName);

        SubCategory sub = ejb.getSubCategory(subId);
        assertEquals((Long) ctgId, sub.getParent().getId());
        assertEquals(subName, sub.getName());
    }

    @Test
    public void testGetAllCategories(){

        long a = ejb.createCategory("a");
        long b = ejb.createCategory("b");
        long c = ejb.createCategory("c");

        ejb.createSubCategory(a, "1");
        ejb.createSubCategory(b, "2");
        ejb.createSubCategory(c, "3");


        List<Category> categories = ejb.getAllCategories(false);
        assertEquals(3, categories.size());

        Category first = categories.get(0);

        try {
            first.getSubCategories().size();
            fail();
        } catch (Exception e){
            //expected
        }

        categories = ejb.getAllCategories(true);

        first = categories.get(0);

        assertEquals(1, first.getSubCategories().size());
    }
}