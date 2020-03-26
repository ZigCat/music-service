package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.MusicController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.models.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class MusicService {
    private static Logger l = LoggerFactory.getLogger(MusicService.class);

    public MusicService() {
    }

    public List<Music> listAll() throws SQLException {
        l.info("getting list of Music");
        return MusicController.musicDao.queryForAll();
    }

    public Music getById(int id) throws SQLException {
        l.info("@@@\tgetting music by id");
        for(Music m: MusicController.musicDao.queryForAll()){
            l.info("Iterating over "+m.toString());
            if(m.getId() == id){
                l.info("@@@\tgetting "+m.toString());
                return m;
            }
        }
        l.warn("@@@\tnothing got, check your code");
        throw new CustomException(Security.badRequestMessage);
    }
}
