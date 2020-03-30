package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.AlbumController;
import com.github.zigcat.ormlite.models.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AlbumService {
    private static Logger l = LoggerFactory.getLogger(AlbumService.class);

    public AlbumService() {
    }

    public List<Album> listAll() throws SQLException {
        l.info("getting list of Albums");
        return AlbumController.albumDao.queryForAll();
    }

    public Album getById(int id) throws SQLException {
        l.info("@@@\tgetting author by id"); //??? Getting Author? TODO(Solomon) fix the typo
        for(Album a: AlbumController.albumDao.queryForAll()){
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
