package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.ormlite.models.TagAlbum;
import com.github.zigcat.services.PaginationService;
import com.github.zigcat.services.Security;
import com.github.zigcat.services.TagAlbumService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TagAlbumController {
    public static Dao<TagAlbum, Integer> taDao;
    private static Logger l = LoggerFactory.getLogger(TagAlbumController.class);
    public static TagAlbumService taService = new TagAlbumService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            taDao = DaoManager.createDao(DatabaseConfiguration.source, TagAlbum.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL TAGALBUM\t!!!");
        Map map = ctx.queryParamMap();
        long page;
        try {
            if(map.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 0;
            }
            l.info("&&&\tgetting all tagAlbum");
            ctx.status(200);
            ctx.result(om.writeValueAsString(paginationService.pagination(taDao, page, 10)));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        } catch (NullPointerException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong query param");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING TAGALBUM BY ID\t!!!");
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            TagAlbum ta = taService.getById(id);
            l.info("&&&\tgetting by id "+ta.toString());
            ctx.result(om.writeValueAsString(ta));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING TAGALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                TagAlbum ta = om.readValue(ctx.body(), TagAlbum.class);
                l.info("&&&\tcreator has access");
                taDao.create(ta);
                ctx.status(201);
                ctx.result(om.writeValueAsString(ta));
            } else {
                ctx.status(403);
                ctx.result("Access denied(403)");
                l.warn(Security.unauthorizedMessage);
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

    public static void update(Context ctx, ObjectMapper om){
        l.info("!!!\tUPDATING TAGALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                TagAlbum tagAlbum = om.readValue(ctx.body(), TagAlbum.class);
                l.info("&&&\tcreator has access");
                for(TagAlbum ta: taService.listAll()){
                    l.info("Iterating over "+ta.toString());
                    if(ta.getId() == tagAlbum.getId()){
                        l.info("&&&\tupdating to "+tagAlbum.toString());
                        taDao.update(tagAlbum);
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(ta));
                    }
                }
            } else {
                ctx.status(403);
                ctx.result("Access denied(403)");
                l.warn(Security.unauthorizedMessage);
            }
        }  catch (JsonProcessingException | SQLException | RedirectionException e) {
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
        l.info("!!!\tDELETING TAGALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map map = ctx.queryParamMap();
        try {
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                if(map.containsKey("id")){
                    int id = Integer.parseInt(ctx.queryParam("id"));l.info("&&&\tcreator has access");
                    for(TagAlbum ta: taService.listAll()){
                        l.info("Iterating over "+ta.toString());
                        if(ta.getId() == id){
                            l.info("&&&\tdeleting to "+ta.toString());
                            taDao.deleteById(id);
                            ctx.status(200);
                            ctx.result(om.writeValueAsString(ta));
                        }
                    }
                } else {
                    throw new NullPointerException();
                }
            } else {
                ctx.status(403);
                ctx.result("Access denied(403)");
                l.warn(Security.unauthorizedMessage);
            }
        }  catch (JsonProcessingException | SQLException | RedirectionException e) {
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
