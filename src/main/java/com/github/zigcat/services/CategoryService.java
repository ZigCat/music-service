package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.CategoryController;
import com.github.zigcat.ormlite.models.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private static Logger l = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService() {
    }

    public List<Category> listAll() throws SQLException {
        l.info("@@@\tgetting list of categories");
        return CategoryController.categoryDao.queryForAll();
    }

    public Category getById(int id) throws SQLException {
        l.info("@@@\tgetting category by id");
        for(Category c: listAll()){
            l.info("Iterating over "+c.toString());
            if(c.getId() == id){
                l.info("getting "+c.toString());
                return c;
            }
        }
        l.info("nothing got");
        return null;
    }
}
