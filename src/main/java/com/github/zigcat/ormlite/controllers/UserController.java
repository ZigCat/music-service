package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.EmailException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.services.PaginationService;
import com.github.zigcat.services.Security;
import com.github.zigcat.services.UserService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserController {
    public static Dao<User, Integer> userDao;
    private static Logger l = LoggerFactory.getLogger(UserController.class);
    public static UserService userService = new UserService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            userDao = DaoManager.createDao(DatabaseConfiguration.source, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om, ObjectMapper omAdmin){
        l.info("!!!\tGETTING ALL INFO\t!!!");
        Map queryMap = ctx.queryParamMap();
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        long page;
        try {
            if(queryMap.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 0;
            }
            List<User> userList = userService.listAll();
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tgetting info as ADMIN");
                ctx.result(omAdmin.writeValueAsString(paginationService.pagination(userDao, page, 10)));
                ctx.status(200);
            }
            else {
                l.info("&&&\tgetting info as USER");
                ctx.result(om.writeValueAsString(paginationService.pagination(userDao, page, 10)));
                ctx.status(200);
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch(NotFoundException e){
            ctx.status(403);
            ctx.result("Generic 403 message");
            l.warn(Security.unauthorizedMessage);
        } catch (NullPointerException e){
            ctx.status(400);
            ctx.result("Wrong queryParam(400)");
            l.warn(Security.badRequestMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om, ObjectMapper omAdmin){
        l.info("!!!\tGETTING INFO BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            for(User u: userService.listAll()){
                l.info("Iterating over "+u.toString());
                if(u.getId() == id){
                    try{
                        if(u.checkUser(Security.authorize(login, password))
                                || Security.authorize(login, password).getRole().equals(Role.ADMIN)) {
                            l.info("&&&\tgetting full info about " + u.toString());
                            ctx.result(omAdmin.writeValueAsString(u));
                            ctx.status(200);
                            break;
                        }
                    } catch (NotFoundException e){
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
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING USER\t!!!");
        try {
            User user = om.readValue(ctx.body(), User.class);
            l.info("&&&\tcreating "+user.toString());
            userDao.create(user);
            ctx.result(om.writeValueAsString(user));
            ctx.status(201);
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (EmailException e){
            ctx.status(400);
            ctx.result("Email isn't valid(400)");
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
            for(User u: userService.listAll()){
                l.info("Iterating over "+u.toString());
                if(u.getId() == updUser.getId()){
                    if(u.checkUser(Security.authorize(login, password))){
                        l.info("&&&\tupdating "+u.toString());
                        String regDate = u.getRegDate();
                        updUser.setRegDate(regDate);
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
        } catch (EmailException e){
            ctx.status(400);
            ctx.result("Email isn't valid(400)");
            l.info(Security.badRequestMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING USER\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map queryMap = ctx.queryParamMap();
        try {
            if(queryMap.containsKey("id")){
                int id = Integer.parseInt(ctx.queryParam("id"));
                for(User u: userService.listAll()){
                    l.info("Iterating over "+u.toString());
                    if(u.getId() == id){
                        if(u.checkUser(Security.authorize(login, password))){
                            l.info("&&&\tdeleting "+ u.toString());
                            userDao.deleteById(id);
                            ctx.result(om.writeValueAsString(u));
                            ctx.status(200);
                            break;
                        }
                    }
                }
            } else {
                l.warn(Security.unauthorizedMessage);
                ctx.status(403);
                ctx.result("Access denied(403)");
            }
        } catch (JsonProcessingException | SQLException e) {
            l.info(Security.serverErrorMessage);
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (EmailException e){
            ctx.status(400);
            ctx.result("Email isn't valid(400)");
            l.info(Security.badRequestMessage);
        } catch (NullPointerException e){
            ctx.status(400);
            ctx.result("Query param isn't valid(400)");
            l.info(Security.badRequestMessage);
        }
    }
}
