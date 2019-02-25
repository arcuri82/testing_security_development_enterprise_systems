package org.tsdes.intro.spring.bean.repository;

import com.google.common.collect.Iterators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by arcuri82 on 25-Feb-19.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository crud;

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

        assertEquals(3, Iterators.size(crud.findAllByCountry("Norway").iterator()));
        assertEquals(1, Iterators.size(crud.findAllByCountry("Sweden").iterator()));
        assertEquals(2, Iterators.size(crud.findAllByCountry("Iceland").iterator()));
    }

    @Test
    public void testGetAllByAuthor() {

        assertEquals(0, crud.count());
        createSomeNews();

        assertEquals(3, Iterators.size(crud.findAllByAuthorId("a").iterator()));
        assertEquals(2, Iterators.size(crud.findAllByAuthorId("b").iterator()));
        assertEquals(1, Iterators.size(crud.findAllByAuthorId("c").iterator()));
    }

    @Test
    public void testGetAllByCountryAndAuthor() {

        assertEquals(0, crud.count());
        createSomeNews();

        assertEquals(2, Iterators.size(crud.findAllByCountryAndAuthorId("Norway", "a").iterator()));
        assertEquals(1, Iterators.size(crud.findAllByCountryAndAuthorId("Sweden", "a").iterator()));
        assertEquals(0, Iterators.size(crud.findAllByCountryAndAuthorId("Iceland", "a").iterator()));
        assertEquals(1, Iterators.size(crud.findAllByCountryAndAuthorId("Norway", "b").iterator()));
        assertEquals(0, Iterators.size(crud.findAllByCountryAndAuthorId("Sweden", "b").iterator()));
        assertEquals(1, Iterators.size(crud.findAllByCountryAndAuthorId("Iceland", "b").iterator()));
        assertEquals(0, Iterators.size(crud.findAllByCountryAndAuthorId("Norway", "c").iterator()));
        assertEquals(0, Iterators.size(crud.findAllByCountryAndAuthorId("Sweden", "c").iterator()));
        assertEquals(1, Iterators.size(crud.findAllByCountryAndAuthorId("Iceland", "c").iterator()));
    }


}