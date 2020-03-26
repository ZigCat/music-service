package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.models.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AuthorService {
    private static Logger l = LoggerFactory.getLogger(AuthorService.class);

    public AuthorService() {
    }

    public List<Author> listAll() throws SQLException {
        l.info("getting list of Authors");
        return AuthorController.authorDao.queryForAll();
    }

    public Author getById(int id) throws SQLException{
        l.info("@@@\tgetting author by id");
        for(Author a: AuthorController.authorDao.queryForAll()){
            l.info("Iterating over "+a.toString());
            if(a.getId() == id){
                l.info("@@@\tgetting "+a.toString());
                return a;
            }
        }
        l.warn("@@@\tnothing got, check your code");
        return null;
    }
}
