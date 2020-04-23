package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.CategoryGenre;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.CategoryGenreService;
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

public class CategoryGenreController {
    public static Dao<CategoryGenre, Integer> cgDao;
    private static Logger l = LoggerFactory.getLogger(CategoryGenreController.class);
    public static CategoryGenreService cgService = new CategoryGenreService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            cgDao = DaoManager.createDao(DatabaseConfiguration.source, CategoryGenre.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL CATEGORYGENRE\t!!!");
        Map map = ctx.queryParamMap();
        long page;
        try {
            if(map.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 0;
            }
            ctx.status(200);
            ctx.result(om.writeValueAsString(paginationService.pagination(cgDao, page, 10)));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING CATEGORYGENRE BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            CategoryGenre categoryGenre = cgService.getById(id);
            ctx.status(200);
            ctx.result(om.writeValueAsString(categoryGenre));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING CATEGORYGENRE\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
            try {
                CategoryGenre cg = om.readValue(ctx.body(), CategoryGenre.class);
                l.info("&&&\tcreating "+cg.toString());
                cgDao.create(cg);
                ctx.status(201);
                ctx.result(om.writeValueAsString(cg));
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
        } else {
            ctx.status(403);
            ctx.result("Access denied(403)");
            l.warn(Security.unauthorizedMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void update(Context ctx, ObjectMapper om){
        l.info("!!!\tUPDATING CATEGORYGENRE\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
            try {
                CategoryGenre cg = om.readValue(ctx.body(), CategoryGenre.class);
                for(CategoryGenre categoryGenre: cgService.listAll()){
                    if(cg.getId() == categoryGenre.getId()){
                        l.info("&&&\tupdating "+categoryGenre.toString()+" to "+cg.toString());
                        cgDao.update(cg);
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(cg));
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
        } else {
            ctx.status(403);
            ctx.result("Access denied(403)");
            l.warn(Security.unauthorizedMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING CATEGORYGENRE\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map map = ctx.queryParamMap();
        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
            try {
                if(map.containsKey("id")){
                    int id = Integer.parseInt(ctx.queryParam("id"));
                    for(CategoryGenre categoryGenre: cgService.listAll()){
                        if(id == categoryGenre.getId()){
                            l.info("&&&\tdeleting "+categoryGenre.toString());
                            cgDao.deleteById(id);
                            ctx.status(200);
                            ctx.result(om.writeValueAsString(categoryGenre));
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
        } else {
            ctx.status(403);
            ctx.result("Access denied(403)");
            l.warn(Security.unauthorizedMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
