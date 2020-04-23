package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.AuthorGroup;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.AuthorGroupService;
import com.github.zigcat.services.PaginationService;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AuthorGroupController {
    public static Dao<AuthorGroup, Integer> agDao;
    private static Logger l = LoggerFactory.getLogger(AuthorGroupController.class);
    public static AuthorGroupService agService = new AuthorGroupService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            agDao = DaoManager.createDao(DatabaseConfiguration.source, AuthorGroup.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL RELATIONS(AUTHOR/GROUP)\t!!!");
        Map map = ctx.queryParamMap();
        long page;
        try {
            if(map.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 1;
            }
            l.info("&&&\tgetting all relations(author/group)");
            ctx.status(200);
            ctx.result(om.writeValueAsString(paginationService.pagitation(agDao, page, 10)));
        } catch (SQLException | JsonProcessingException e) {
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
            e.printStackTrace();
        } catch (NullPointerException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong query param");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING RELATION(AUTHOR/GROUP) BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            AuthorGroup ag = agService.getById(id);
            l.info("&&&\tgetting "+ag.toString());
            ctx.result(om.writeValueAsString(ag));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException e) {
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
            e.printStackTrace();
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                AuthorGroup authorGroup = om.readValue(ctx.body(), AuthorGroup.class);
                agDao.create(authorGroup);
                ctx.status(201);
                ctx.result(om.writeValueAsString(authorGroup));
                l.info("&&&\tcreating relation author/group");
            } else {
                ctx.status(403);
                l.warn(Security.unauthorizedMessage);
                ctx.result("Access denied");
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong input data(400)");
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("One of NotNull params is Null(400)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void update(Context ctx, ObjectMapper om){
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreator has access");
                AuthorGroup authorGroup = om.readValue(ctx.body(), AuthorGroup.class);
                List<AuthorGroup> agList = agService.listAll();
                for(AuthorGroup ag: agList){
                    l.info("Iterating over "+ag.toString());
                    if(ag.getId() == authorGroup.getId()){
                        agDao.update(authorGroup);
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(authorGroup));
                        l.info("&&&\tupdating relation author/group");
                    }
                }
            } else {
                ctx.status(403);
                l.warn(Security.unauthorizedMessage);
                ctx.result("Access denied");
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong input data(400)");
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("One of NotNull params is Null(400)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om) {
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map map = ctx.queryParamMap();
        try {
            if (Security.authorize(login, password).getRole().equals(Role.ADMIN)) {
                if(map.containsKey("id")){
                    int id = Integer.parseInt(ctx.queryParam("id"));
                    l.info("&&&\tcreator has access");
                    AuthorGroup authorGroup = om.readValue(ctx.body(), AuthorGroup.class);
                    List<AuthorGroup> agList = agService.listAll();
                    for (AuthorGroup ag : agList) {
                        l.info("Iterating over " + ag.toString());
                        if (ag.getId() == authorGroup.getId()) {
                            agDao.deleteById(authorGroup.getId());
                            ctx.status(200);
                            ctx.result(om.writeValueAsString(authorGroup));
                            l.info("&&&\tdeleting relation author/group");
                        }
                    }
                }
            } else {
                ctx.status(403);
                l.warn(Security.unauthorizedMessage);
                ctx.result("Access denied");
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong input data(400)");
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
