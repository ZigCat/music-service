package com.github.zigcat.services;

import com.github.zigcat.ormlite.controllers.UserMusicController;
import com.github.zigcat.ormlite.models.UserMusic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class UserMusicService {
    private static Logger l = LoggerFactory.getLogger(UserMusicService.class);

    public UserMusicService() {
    }

    public List<UserMusic> listAll() throws SQLException {
        l.info("getting list of UserMusic");
        return UserMusicController.umDao.queryForAll();
    }
}
