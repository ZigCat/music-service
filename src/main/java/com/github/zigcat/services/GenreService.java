package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.GenreController;
import com.github.zigcat.ormlite.models.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class GenreService {
    private static Logger l = LoggerFactory.getLogger(GenreService.class);

    public GenreService() {
    }

    public List<Genre> listAll() throws SQLException {
        l.info("getting list of Genres");
        return GenreController.genreDao.queryForAll();
    }

    public Genre getById(int id) throws SQLException {
        l.info("@@@\tgetting genre by id");
        for(Genre g: GenreController.genreDao.queryForAll()){
            l.info("Iterating over "+g.toString());
            if(g.getId() == id){
                l.info("@@@\tgetting "+g.toString());
                return g;
            }
        }
        l.warn("@@@\tnothing got, check your code");
        return null;
    }
}
