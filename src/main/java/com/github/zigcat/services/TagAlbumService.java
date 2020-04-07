package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.TagAlbumController;
import com.github.zigcat.ormlite.models.TagAlbum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class TagAlbumService {
    private static Logger l = LoggerFactory.getLogger(TagService.class);

    public TagAlbumService() {
    }

    public List<TagAlbum> listAll() throws SQLException {
        return TagAlbumController.taDao.queryForAll();
    }

    public TagAlbum getById(int id) throws SQLException {
        l.info("@@@\tgetting tagAlbum by id");
        for(TagAlbum ta: listAll()){
            l.info("Iterating over "+ta.toString());
            if(ta.getId() == id){
                l.info("@@@\tgetting "+ta.toString());
                return ta;
            }
        }
        l.info("nothing got");
        return null;
    }
}
