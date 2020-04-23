package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Album;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.AlbumService;
import com.github.zigcat.services.PaginationService;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

public class AlbumController {
    public static Dao<Album, Integer> albumDao;
    private static Logger l = LoggerFactory.getLogger(AlbumController.class);
    public static AlbumService albumService = new AlbumService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            albumDao = DaoManager.createDao(DatabaseConfiguration.source, Album.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL ALBUMS\t!!!");
        Map queryMap = ctx.queryParamMap();
        long page;
        try {
            if(queryMap.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 1;
            }
            l.info("&&&\tgetting all albums");
            ctx.result(om.writeValueAsString(paginationService.pagitation(albumDao, page, 10)));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException e) {
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
            e.printStackTrace();
        } catch (NullPointerException e){
            ctx.status(400);
            ctx.result("Wrong queryParam(400)");
            l.warn(Security.badRequestMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            for(Album a: albumService.listAll()){
                if(a.getId() == id){
                    l.info("&&&\tgetting info about "+ a.toString());
                    ctx.result(om.writeValueAsString(a));
                    ctx.status(200);
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
            e.printStackTrace();
        }
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING ALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Album album = om.readValue(ctx.body(), Album.class);
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                albumDao.create(album);
                l.info("&&&\tcreator has access, creating album "+album.toString());
                ctx.result(om.writeValueAsString(album));
                ctx.status(201);
            } else {
                ctx.result("Access denied");
                l.warn(Security.unauthorizedMessage);
                ctx.status(403);
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
        l.info("!!!\tUPDATING ALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Album album = om.readValue(ctx.body(), Album.class);
            for(Album a: albumService.listAll()){
                l.info("Iterating over "+a.toString());
                if(a.getId() == album.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, updating album "+a.toString());
                        albumDao.update(album);
                        ctx.result(om.writeValueAsString(album));
                        ctx.status(200);
                        l.info("&&&\talbum updated to "+album.toString());
                        break;
                    } else {
                        ctx.result("Access denied");
                        l.warn(Security.unauthorizedMessage);
                        ctx.status(403);
                    }
                }
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.result("Wrong input data(400)");
            ctx.status(400);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("One of NotNull params is Null(400)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING ALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map queryMap = ctx.queryParamMap();
        try {
            if(queryMap.containsKey("id")){
                int id = Integer.parseInt(ctx.queryParam("id"));
                for(Album a: albumService.listAll()){
                    if(a.getId() == id){
                        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                            albumDao.deleteById(id);
                            l.info("&&&\tcreator has access, deleting album "+a.toString());
                            ctx.result(om.writeValueAsString(a));
                            ctx.status(200);
                            break;
                        } else {
                            ctx.result("Access denied");
                            l.warn(Security.unauthorizedMessage);
                            ctx.status(403);
                        }
                    }
                }
            } else {
                throw new NullPointerException("wrong queryParam");
            }
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.result("Wrong input data(400)");
            ctx.status(400);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.result("One of NotNull params is Null(400)");
            ctx.status(400);
        } catch (NullPointerException e){
            l.warn(Security.badRequestMessage);
            ctx.result("Wrong query param(400)");
            ctx.status(400);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
