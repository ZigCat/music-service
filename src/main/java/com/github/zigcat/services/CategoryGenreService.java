package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.CategoryGenreController;
import com.github.zigcat.ormlite.models.Category;
import com.github.zigcat.ormlite.models.CategoryGenre;
import com.github.zigcat.ormlite.models.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryGenreService {
    private static Logger l = LoggerFactory.getLogger(CategoryGenreService.class);

    public CategoryGenreService() {
    }

    public List<CategoryGenre> listAll() throws SQLException {
        l.info("@@@\tgetting all categoryGenre");
        return CategoryGenreController.cgDao.queryForAll();
    }

    public CategoryGenre getById(int id) throws SQLException {
        l.info("@@@\tgetting categoryGenre by id");
        for(CategoryGenre cg: listAll()){
            l.info("Iterating over "+cg.toString());
            if(id == cg.getId()){
                l.info("&&&\tgetting "+cg.toString());
                return cg;
            }
        }
        l.info("nothing got");
        return null;
    }

    public ArrayList<Category> getByGenre(Genre genre) throws SQLException {
        l.info("@@@\tgetting categories by genre");
        ArrayList<Category> categories = new ArrayList<>();
        for(CategoryGenre cg: CategoryGenreController.cgService.listAll()){
            if(cg.getGenre().getId() == genre.getId()){
                l.info("@@@\tadding category "+cg.getCategory().toString());
                categories.add(cg.getCategory());
            }
        }
        return categories;
    }
}
