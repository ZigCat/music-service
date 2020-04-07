package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Genre;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.GenreService;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class GenreController {
    public static Dao<Genre, Integer> genreDao;
    private static Logger l = LoggerFactory.getLogger(GenreController.class);
    public static GenreService genreService = new GenreService();

    static {
        try {
            genreDao = DaoManager.createDao(DatabaseConfiguration.source, Genre.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL GENRES\t!!!");
        try {
            List<Genre> genreList = genreService.listAll();
            l.info("&&&\tgetting all genres");
            ctx.result(om.writeValueAsString(genreList));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING GENRE BY ID\t!!!");
        int id = Integer.parseInt(ctx.body());
        try {
            for(Genre g: genreService.listAll()){
                l.info("Iterating over "+g.toString());
                if(g.getId() == id){
                    l.info("&&&\tgetting info about "+g.toString());
                    ctx.result(om.writeValueAsString(g));
                    ctx.status(200);
                    break;
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING NEW GENRE\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Genre genre = om.readValue(ctx.body(), Genre.class);
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreator has access, creating new genre "+genre.toString());
                ctx.status(201);
                genreDao.create(genre);
                ctx.result(om.writeValueAsString(genre));
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
        l.info("!!!\tUPDATING GENRE\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Genre updGenre = om.readValue(ctx.body(), Genre.class);
            for(Genre g: genreService.listAll()){
                l.info("Iterating over "+g.toString());
                if(g.getId() == updGenre.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, updating genre "+g.toString());
                        genreDao.update(updGenre);
                        ctx.result(om.writeValueAsString(updGenre));
                        ctx.status(200);
                        l.info("&&&\tupdated to "+updGenre.toString());
                        break;
                    }
                }
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
        l.info("!!!\tDELETING GENRE\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Genre delGenre = om.readValue(ctx.body(), Genre.class);
            for(Genre g: genreService.listAll()){
                l.info("Iterating over "+g.toString());
                if(g.getId() == delGenre.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, deleting genre "+g.toString());
                        genreDao.deleteById(g.getId());
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(g));
                        break;
                    }
                }
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
}
