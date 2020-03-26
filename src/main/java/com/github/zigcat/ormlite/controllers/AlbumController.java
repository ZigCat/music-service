package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Album;
import com.github.zigcat.ormlite.models.Author;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.AlbumService;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AlbumController {
    public static Dao<Album, Integer> albumDao;
    private static Logger l = LoggerFactory.getLogger(AlbumController.class);
    public static AlbumService albumService = new AlbumService();

    static {
        try {
            albumDao = DaoManager.createDao(DatabaseConfiguration.source, Album.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL ALBUMS\t!!!");
        try {
            l.info("&&&\tgetting all albums");
            List<Album> albumList = albumService.listAll();
            ctx.result(om.writeValueAsString(albumList));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException e) {
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
            e.printStackTrace();
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
            }
        } catch (JsonProcessingException | SQLException e) {
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
            e.printStackTrace();
        } catch (NotFoundException e){
            ctx.status(401);
            ctx.result("Generic 401 message");
            l.warn(Security.unauthorizedMessage);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Generic 400 message");
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
            ctx.result("Generic 401 message");
            ctx.status(401);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Generic 400 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING ALBUM\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Album album = om.readValue(ctx.body(), Album.class);
            for(Album a: albumService.listAll()){
                if(a.checkAlbum(album)){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        albumDao.delete(a);
                        l.info("&&&\tcreator has access, deleting album "+a.toString());
                        ctx.result(om.writeValueAsString(a));
                        ctx.status(200);
                        break;
                    }
                }
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NotFoundException e){
            l.warn(Security.unauthorizedMessage);
            ctx.result("Generic 401 message");
            ctx.status(401);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.result("Genetic 400 message");
            ctx.status(400);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
