package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.TagController;
import com.github.zigcat.ormlite.models.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class TagService {

    private static Logger l = LoggerFactory.getLogger(TagService.class);

    public TagService() {
    }

    public List<Tag> listAll() throws SQLException {
        l.info("getting list of tags");
        return TagController.tagDao.queryForAll();
    }

    public Tag getById(int id) throws SQLException {
        l.info("@@@\tgetting tag by id");
        for(Tag t: listAll()){
            l.info("Iterating over "+t.toString());
            if(t.getId() == id){
                l.info("@@@\tgetting "+t.toString());
                return t;
            }
        }
        l.info("nothing got");
        return null;
    }
}
