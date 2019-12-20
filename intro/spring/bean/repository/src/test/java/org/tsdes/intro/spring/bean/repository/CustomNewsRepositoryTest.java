package org.tsdes.intro.spring.bean.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by arcuri82 on 20-Dec-19.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomNewsRepositoryTest {

    @Autowired
    private CustomNewsRepository crud;

    @BeforeEach
    public void cleanDatabase(){
        crud.deleteAll();
    }

    @Test
    public void testInitialization(){
        assertNotNull(crud);
    }

    private Long createNews(String author, String text, String country){

        NewsEntity news = new NewsEntity();
        news.setAuthorId(author);
        news.setText(text);
        news.setCountry(country);

        crud.save(news);

        return news.getId();
    }

    @Test
    public void testChangeText(){

        String foo = "foo";
        String bar = "bar";

        long id = createNews("a", foo, "Norway");

        assertEquals(foo, crud.findById(id).get().getText());

        crud.changeText(id, bar);

        assertEquals(bar, crud.findById(id).get().getText());
    }




    //------ same tests as in NewsRepositoryTest -----------------

    @Test
    public void testCreate() {

        assertEquals(0, crud.count());

        Long id = createNews("author", "someText", "Norway");

        assertEquals(1, crud.count());
        assertEquals(id, crud.findById(id).get().getId());
    }

    @Test
    public void testDelete() {

        assertEquals(0, crud.count());

        Long id = createNews("author", "text", "Norway");
        assertTrue(crud.existsById(id));
        assertEquals(1, crud.count());

        crud.deleteById(id);

        assertFalse(crud.existsById(id));
        assertEquals(0, crud.count());
    }

    @Test
    public void testGet() {

        String author = "author";
        String text = "someText";
        String country = "Norway";

        Long id = createNews(author, text, country);
        NewsEntity news = crud.findById(id).get();

        assertEquals(author, news.getAuthorId());
        assertEquals(text, news.getText());
        assertEquals(country, news.getCountry());
    }




    private void createSomeNews() {
        createNews("a", "text", "Norway");
        createNews("a", "other text", "Norway");
        createNews("a", "more text", "Sweden");
        createNews("b", "text", "Norway");
        createNews("b", "yet another text", "Iceland");
        createNews("c", "text", "Iceland");
    }

    @Test
    public void testFindAll() {

        assertEquals(0, crud.count());
        createSomeNews();

        assertEquals(6, crud.count());
    }

    @Test
    public void testGetAllByCountry() {

        assertEquals(0,crud.count());
        createSomeNews();

        assertEquals(3, crud.findAllByCountry("Norway").size());
        assertEquals(1, crud.findAllByCountry("Sweden").size());
        assertEquals(2, crud.findAllByCountry("Iceland").size());
    }

    @Test
    public void testGetAllByAuthor() {

        assertEquals(0, crud.count());
        createSomeNews();

        assertEquals(3, crud.findAllByAuthorId("a").size());
        assertEquals(2, crud.findAllByAuthorId("b").size());
        assertEquals(1, crud.findAllByAuthorId("c").size());
    }

    @Test
    public void testGetAllByCountryAndAuthor() {

        assertEquals(0, crud.count());
        createSomeNews();

        assertEquals(2, crud.findAllByCountryAndAuthorId("Norway", "a").size());
        assertEquals(1, crud.findAllByCountryAndAuthorId("Sweden", "a").size());
        assertEquals(0, crud.findAllByCountryAndAuthorId("Iceland", "a").size());
        assertEquals(1, crud.findAllByCountryAndAuthorId("Norway", "b").size());
        assertEquals(0, crud.findAllByCountryAndAuthorId("Sweden", "b").size());
        assertEquals(1, crud.findAllByCountryAndAuthorId("Iceland", "b").size());
        assertEquals(0, crud.findAllByCountryAndAuthorId("Norway", "c").size());
        assertEquals(0, crud.findAllByCountryAndAuthorId("Sweden", "c").size());
        assertEquals(1, crud.findAllByCountryAndAuthorId("Iceland", "c").size());
    }

    @Test
    public void testCustomQuery() {

        assertEquals(0, crud.count());
        createSomeNews();

        assertEquals(2, crud.customQuery("Norway", "a").size());
        assertEquals(1, crud.customQuery("Sweden", "a").size());
        assertEquals(0, crud.customQuery("Iceland", "a").size());
        assertEquals(1, crud.customQuery("Norway", "b").size());
        assertEquals(0, crud.customQuery("Sweden", "b").size());
        assertEquals(1, crud.customQuery("Iceland", "b").size());
        assertEquals(0, crud.customQuery("Norway", "c").size());
        assertEquals(0, crud.customQuery("Sweden", "c").size());
        assertEquals(1, crud.customQuery("Iceland", "c").size());
    }


}