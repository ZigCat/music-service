package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.UserMusic;
import com.github.zigcat.services.Security;
import com.github.zigcat.services.UserMusicService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class UserMusicController {
    public static Dao<UserMusic, Integer> umDao;
    private static Logger l = LoggerFactory.getLogger(UserMusicController.class);
    public static UserMusicService umService = new UserMusicService();

    static {
        try {
            umDao = DaoManager.createDao(DatabaseConfiguration.source, UserMusic.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL USERMUSIC\t!!!");
        try {
            List<UserMusic> umList = umService.listAll();
            l.info("&&&\tgetting all usermusic");
            ctx.status(200);
            ctx.result(om.writeValueAsString(umList));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.badRequestMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING USERMUSIC BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            for(UserMusic um : umService.listAll()){
                l.info("Iterating over "+um.toString());
                if(um.getId() == id){
                    l.info("&&&\tgetting info about "+um.toString());
                    ctx.status(200);
                    ctx.result(om.writeValueAsString(um));
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.badRequestMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void addMusic(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING USERMUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            UserMusic um = om.readValue(ctx.body(), UserMusic.class);
            if(um.getUser().checkUser(Security.authorize(login, password))){
                l.info("&&&\tcreator has access, creating usermusic "+um.toString());
                umDao.create(um);
                ctx.status(201);
                ctx.result(om.writeValueAsString(um));
            } else {
                l.info(Security.unauthorizedMessage);
                ctx.status(401);
                ctx.result("Generic 401 message");
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            l.warn(Security.badRequestMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NotFoundException e){
            l.warn(Security.unauthorizedMessage);
            ctx.status(401);
            ctx.result("Generic 401 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING USERMUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            UserMusic delUm = om.readValue(ctx.body(), UserMusic.class);
            for(UserMusic um: umService.listAll()){
                l.info("Iterating over "+um.toString());
                if(um.getId() == delUm.getId()){
                    if(um.getUser().checkUser(Security.authorize(login, password))){
                        l.info("&&&\tcreator has access, deleting usermusic "+um.toString());
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(um));
                        umDao.deleteById(delUm.getId());
                    }
                }
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            l.warn(Security.badRequestMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NotFoundException e){
            l.warn(Security.unauthorizedMessage);
            ctx.status(401);
            ctx.result("Generic 401 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
