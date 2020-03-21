package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Music;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class MusicController {
    public static Dao<Music, Integer> musicDao;
    private static Logger l = LoggerFactory.getLogger(MusicController.class);

    static {
        try {
            musicDao = DaoManager.createDao(DatabaseConfiguration.source, Music.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL MUSIC\t!!!");
        try {
            List<Music> musicList = musicDao.queryForAll();
            l.info("&&&\tgetting all music");
            ctx.status(200);
            ctx.result(om.writeValueAsString(musicList));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING MUSIC BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            for(Music m: musicDao.queryForAll()){
                l.info("Iterating over "+m.toString());
                if(m.getId() == id){
                    l.info("&&&\tgetting music file:"+m.toString());
                    ctx.status(200);
                    ctx.result(new FileInputStream(new File(m.getContent())));
                    break;
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        }
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING NEW MUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Music music = om.readValue(ctx.body(), Music.class);
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreator has access, creating music "+music.toString());
                ctx.status(201);
                ctx.result(om.writeValueAsString(music));
                musicDao.create(music);
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch (NotFoundException e){
            l.warn(Security.unauthorizedMessage);
            ctx.status(401);
            ctx.result("Generic 401 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void update(Context ctx, ObjectMapper om){
        l.info("!!!\tUPDATING MUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Music music = om.readValue(ctx.body(), Music.class);
            for(Music m: musicDao.queryForAll()){
                if(m.getId() == music.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, updating music "+m.toString());
                        musicDao.update(music);
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(music));
                        l.info("&&&\tupdated to "+music.toString());
                        break;
                    }
                }
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch (NotFoundException e){
            l.warn(Security.unauthorizedMessage);
            ctx.status(401);
            ctx.result("Generic 401 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING MUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Music music = om.readValue(ctx.body(), Music.class);
            for(Music m: musicDao.queryForAll()){
                if(m.getId() == music.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, deleting music "+m.toString());
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(m));
                        musicDao.deleteById(m.getId());
                        break;
                    }
                }
            }
        }  catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch (NotFoundException e){
            l.warn(Security.unauthorizedMessage);
            ctx.status(401);
            ctx.result("Generic 401 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
