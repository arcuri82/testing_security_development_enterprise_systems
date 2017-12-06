package org.tsdes.jee.exercises.quizgame.backend.ejb;


import org.tsdes.jee.exercises.quizgame.backend.entity.Category;
import org.tsdes.jee.exercises.quizgame.backend.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by arcuri82 on 30-Nov-16.
 */
@Stateless
public class CategoryEjb {

    @PersistenceContext
    private EntityManager em;

    public Long createCategory(String name){

        Category category = new Category();
        category.setName(name);

        em.persist(category);

        return category.getId();
    }


    public Long createSubCategory(long parentId, String name){

        Category category = em.find(Category.class, parentId);
        if(category == null){
            throw new IllegalArgumentException("Category with id "+parentId+" does not exist");
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setParent(category);
        em.persist(subCategory);

        category.getSubCategories().add(subCategory);

        return subCategory.getId();
    }


    public List<Category> getAllCategories(boolean withSub){

        Query query = em.createQuery("select c from Category c");
        List<Category> categories = query.getResultList();

        if(withSub){
            //force loading
            categories.stream().forEach(c -> c.getSubCategories().size());
        }

        return categories;
    }

    public boolean doesCategoryExist(long id){
        Category category = em.find(Category.class, id);
        return category != null;
    }

    public Void changeCategoryName(long id, String name){
        Category category = em.find(Category.class, id);
        if(category != null){
            category.setName(name);
        }
        return null;
    }

    public Category getCategory(long id, boolean withSub){

        Category category = em.find(Category.class, id);
        if(withSub && category != null){
            category.getSubCategories().size();
        }

        return category;
    }

    public boolean deleteCategory(long id){
        Category category = em.find(Category.class, id);
        if(category != null){
            em.remove(category);
            return true;
        }
        return false;
    }

    public SubCategory getSubCategory(long id){

        return em.find(SubCategory.class, id);
    }

    public List<SubCategory> getSubCategories(Long parentId){

        Query query;
        if(parentId == null){
            query = em.createQuery("select s from SubCategory s");
        } else {
            query = em.createQuery("select s from SubCategory s where s.parent.id=?1");
            query.setParameter(1, parentId);
        }

        return query.getResultList();
    }

}
