package org.tsdes.intro.spring.bean.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by arcuri82 on 25-Feb-19.
 * <p>
 * Recall CRUD means: Create, Read, Update and Delete.
 * <p>
 * By using @Repository annotation on an interface that extends  CrudRepository,
 * Spring will give a bean initialized to do operation on the given Entity
 * in the database, like save(), exists(), findAll(), etc.
 * So a lot of free stuff with a single line of code.
 * <p>
 * Furthermore, instead of creating JPQL queries manually, and methods in a EJB to
 * call them, here Spring can automatically generate code in the proxied classes based
 * on the name of the functions.
 * For example, if you define a method findAllByCountry (which is just a signature in
 * this interface), Spring analyse the method name and will create a method that does
 * query on database for all the entities from the given input country.
 * If you misspell a property, you will get runtime errors.
 * So it is important to have tests for them (which you should have anyway...).
 * However, IntelliJ does automatically check the names of those functions, and flags
 * warnings if those methods are misspelled
 */
@Repository
public interface NewsRepository extends CrudRepository<NewsEntity, Long> {

    List<NewsEntity> findAllByCountry(String country);


    List<NewsEntity> findAllByAuthorId(String authorId);

    /*
        You can have more sophisticated queries by using connecting words like "And".
        This is very convenient for simple queries, but not really for complex ones.

        See Spring documentation, eg:
        https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
     */
    List<NewsEntity> findAllByCountryAndAuthorId(String country, String authorId);

    /*
     * Instead of relying on the method name, you can also create custom
     * queries with the @Query annotation
     */
    @Query("select n from NewsEntity n where n.country=?1 and n.authorId=?2")
    List<NewsEntity> customQuery(String country, String authorId);

}
