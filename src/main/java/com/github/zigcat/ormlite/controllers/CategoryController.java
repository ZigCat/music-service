package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Category;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.CategoryService;
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

public class CategoryController {
    public static Dao<Category, Integer> categoryDao;
    private static Logger l = LoggerFactory.getLogger(CategoryController.class);
    public static CategoryService categoryService = new CategoryService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            categoryDao = DaoManager.createDao(DatabaseConfiguration.source, Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL CATEGORIES\t!!!");
        Map map = ctx.queryParamMap();
        long page;
        try {
            if(map.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 1;
            }
            l.info("&&&\tgetting all categories");
            ctx.status(200);
            ctx.result(om.writeValueAsString(paginationService.pagitation(categoryDao, page, 10)));
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
        l.info("!!!\tGETTING CATEGORY BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            Category category = categoryService.getById(id);
            l.info("&&&\tgetting category by id "+category.toString());
            ctx.result(om.writeValueAsString(category));
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
        l.info("!!!\tCREATING CATEGORY\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
            try {
                Category category = om.readValue(ctx.body(), Category.class);
                categoryDao.create(category);
                l.info("&&&\tcreating category "+category.toString());
                ctx.status(201);
                ctx.result(om.writeValueAsString(category));
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
            l.warn(Security.unauthorizedMessage);
            ctx.result("Access denied(403)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void update(Context ctx, ObjectMapper om){
        l.info("!!!\tUPDATING CATEGORY\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
            try {
                Category category = om.readValue(ctx.body(), Category.class);
                for(Category c: categoryService.listAll()){
                    if(category.getId() == c.getId()){
                        categoryDao.update(category);
                        l.info("updating "+c.toString()+" to "+category.toString());
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(category));
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
            l.warn(Security.unauthorizedMessage);
            ctx.result("Access denied(403)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING CATEGORY\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map map = ctx.queryParamMap();
        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
            try {
                if(map.containsKey("id")){
                    int id = Integer.parseInt(ctx.queryParam("id"));
                    for(Category c: categoryService.listAll()){
                        if(id == c.getId()){
                            categoryDao.deleteById(id);
                            l.info("deleting "+c.toString());
                            ctx.status(200);
                            ctx.result(om.writeValueAsString(c));
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
            } catch (NullPointerException e){
                l.warn(Security.badRequestMessage);
                ctx.status(400);
                ctx.result("Wrong query param");
            }
        } else {
            ctx.status(403);
            l.warn(Security.unauthorizedMessage);
            ctx.result("Access denied(403)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
