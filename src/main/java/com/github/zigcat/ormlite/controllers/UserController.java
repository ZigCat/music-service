package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    public static Dao<User, Integer> userDao;
    private static Logger l = LoggerFactory.getLogger(UserController.class);

    static {
        try {
            userDao = DaoManager.createDao(DatabaseConfiguration.source, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om, ObjectMapper omAdmin){
        l.info("!!!\tGETTING ALL INFO\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            List<User> userList = userDao.queryForAll();
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tgetting info as ADMIN");
                ctx.result(omAdmin.writeValueAsString(userList));
                ctx.status(200);
            }
            else {
                l.info("&&&\tgetting info as USER");
                ArrayList<User> usArray = new ArrayList<>();
                ArrayList<User> uArray = new ArrayList<>();
                for(User u: userList){
                    l.info("Iterating over "+u.toString());
                    if(u.checkUser(Security.authorize(login, password))) {
                        l.info("&&&\tgetting full info about "+u.toString());
                        uArray.add(u);
                    } else {
                        usArray.add(u);
                    }
                }
                ctx.result(omAdmin.writeValueAsString(uArray) + om.writeValueAsString(usArray));
                ctx.status(200);
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch(NotFoundException e){
            ctx.status(401);
            ctx.result("Generic 401 message");
            l.warn(Security.unauthorizedMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om, ObjectMapper omAdmin){
        l.info("!!!\tGETTING INFO BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            for(User u: userDao.queryForAll()){
                l.info("Iterating over "+u.toString());
                if(u.getId() == id){
                    if(u.checkUser(Security.authorize(login, password))
                            && Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tgetting full info about "+u.toString());
                        ctx.result(omAdmin.writeValueAsString(u));
                        ctx.status(200);
                        break;
                    } else {
                        l.info("&&&\tgetting info about "+u.toString());
                        ctx.result(om.writeValueAsString(u));
                        ctx.status(200);
                        break;
                    }
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch (NotFoundException e){
            ctx.status(401);
            ctx.result("Generic 401 message");
            l.warn(Security.unauthorizedMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING USER\t!!!");
        try {
            User user = om.readValue(ctx.body(), User.class);
            if(Security.isValidEmail(user.getEmail()) && !user.getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreating "+user.toString());
                userDao.create(user);
                ctx.result(om.writeValueAsString(user));
                ctx.status(201);
            } else {
                ctx.status(400);
                ctx.result("Generic 400 message");
                throw new CustomException("Email isn't valid");
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (NotFoundException e){
            ctx.status(400);
            ctx.result("Generic 400 message");
            l.warn(Security.badRequestMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void update(Context ctx, ObjectMapper om){
        l.info("!!!\tUPDATING USER\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            User updUser = om.readValue(ctx.body(), User.class);
            for(User u: userDao.queryForAll()){
                l.info("Iterating over "+u.toString());
                if(u.getId() == updUser.getId()){
                    if(u.checkUser(Security.authorize(login, password))){
                        l.info("&&&\tupdating "+u.toString());
                        userDao.update(updUser);
                        l.info("&&&\tupdated to "+updUser.toString());
                        ctx.result(om.writeValueAsString(updUser));
                        ctx.status(200);
                        break;
                    }
                }
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (NotFoundException e){
            ctx.status(401);
            ctx.result("Generic 401 message");
            l.info(Security.unauthorizedMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING USER\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            User delUser = om.readValue(ctx.body(), User.class);
            for(User u: userDao.queryForAll()){
                l.info("Iterating over "+u.toString());
                if(u.checkUser(delUser)){
                    if(u.checkUser(Security.authorize(login, password))){
                        l.info("&&&\tdeleting "+delUser.toString());
                        userDao.delete(u);
                        ctx.result(om.writeValueAsString(u));
                        ctx.status(200);
                        break;
                    }
                }
            }
        } catch (JsonProcessingException | SQLException e) {
            l.info(Security.serverErrorMessage);
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NotFoundException e){
            ctx.status(401);
            ctx.result("Generic 401 message");
            l.info(Security.unauthorizedMessage);
        }
    }
}
