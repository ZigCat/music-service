package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.ormlite.models.UserMusic;
import com.github.zigcat.services.PaginationService;
import com.github.zigcat.services.Security;
import com.github.zigcat.services.UserMusicService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserMusicController {
    public static Dao<UserMusic, Integer> umDao;
    private static Logger l = LoggerFactory.getLogger(UserMusicController.class);
    public static UserMusicService umService = new UserMusicService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            umDao = DaoManager.createDao(DatabaseConfiguration.source, UserMusic.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL USERMUSIC\t!!!");
        Map map = ctx.queryParamMap();
        ArrayList<User> users = new ArrayList<>();
        int user;
        try {
            if(map.containsKey("user")){
                user = Integer.parseInt(ctx.queryParam("page"));
            } else {
                throw new NullPointerException();
            }
            for(UserMusic um: umService.listAll()){
                if(um.getUser().getId() == user){
                    users.add(um.getUser());
                }
            }
            l.info("&&&\tgetting usermusic of user with id "+user);
            ctx.status(200);
            ctx.result(om.writeValueAsString(users));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.badRequestMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NullPointerException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong query param");
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
                ctx.status(403);
                ctx.result("Generic 403 message");
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
            e.printStackTrace();
        } catch (NotFoundException e){
            ctx.status(400);
            ctx.result("Wrong input data(400)");
            l.warn(Security.badRequestMessage);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("One of NotNull params is Null(400)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING USERMUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map map = ctx.queryParamMap();
        try {
            if(map.containsKey("id")){
                int id = Integer.parseInt(ctx.queryParam("id"));
                for(UserMusic um: umService.listAll()){
                    l.info("Iterating over "+um.toString());
                    if(um.getId() == id){
                        if(um.getUser().checkUser(Security.authorize(login, password))){
                            l.info("&&&\tcreator has access, deleting usermusic "+um.toString());
                            ctx.status(200);
                            ctx.result(om.writeValueAsString(um));
                            umDao.deleteById(id);
                        }
                    }
                }
            } else {
                throw new NullPointerException();
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
            e.printStackTrace();
        } catch (NotFoundException e){
            ctx.status(400);
            ctx.result("Wrong input data(400)");
            l.warn(Security.badRequestMessage);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("One of NotNull params is Null(400)");
        } catch (NullPointerException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong query param");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
