package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.AuthorGroupController;
import com.github.zigcat.ormlite.models.AuthorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AuthorGroupService {
    private static Logger l = LoggerFactory.getLogger(AuthorGroupService.class);

    public AuthorGroupService() {
    }

    public List<AuthorGroup> listAll() throws SQLException {
        l.info("getting list of relations(author/group)");
        return AuthorGroupController.agDao.queryForAll();
    }

    public AuthorGroup getById(int id) throws SQLException {
        l.info("@@@\tgetting relation(author/group) by id");
        for(AuthorGroup ag: AuthorGroupController.agDao.queryForAll()){
            l.info("Iterating over "+ag.toString());
            if(ag.getId() == id){
                l.info("@@@\tgetting "+ag.toString());
                return ag;
            }
        }
        l.info("@@@\tnothing got");
        return null;
    }
}
