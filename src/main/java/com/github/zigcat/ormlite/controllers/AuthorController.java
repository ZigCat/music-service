package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Author;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.AuthorService;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AuthorController {
    public static Dao<Author, Integer> authorDao;
    private static Logger l = LoggerFactory.getLogger(AuthorController.class);
    public static AuthorService authorService = new AuthorService();

    static {
        try {
            authorDao = DaoManager.createDao(DatabaseConfiguration.source, Author.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL AUTHORS\t!!!");
        try {
            List<Author> authorList = authorService.listAll();
            l.info("&&&\tgetting info about all authors");
            ctx.result(om.writeValueAsString(authorList));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException e) {
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
            e.printStackTrace();
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING AUTHOR BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            for(Author a: authorService.listAll()){
                l.info("Iterating over "+a.toString());
                if(a.getId() == id){
                    l.info("&&&\tgetting info about "+a.toString());
                    ctx.result(om.writeValueAsString(a));
                    ctx.status(200);
                    break;
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
        l.info("!!!\tCREATING AUTHOR\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Author author = om.readValue(ctx.body(), Author.class);
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreator has access, creating author "+author.toString());
                authorDao.create(author);
                ctx.result(om.writeValueAsString(author));
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
        l.info("!!!\tUPDATING AUTHOR\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Author author = om.readValue(ctx.body(), Author.class);
            for(Author a: authorService.listAll()){
                l.info("Iterating over "+a.toString());
                if(a.getId() == author.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, updating author "+a.toString());
                        authorDao.update(author);
                        ctx.result(om.writeValueAsString(author));
                        ctx.status(200);
                        l.info("&&&\tauthor updated to "+author.toString());
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
        }catch (NotFoundException e){
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

    public static void delete(Context ctx, ObjectMapper om){
        l.info("!!!\tDELETING AUTHOR\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Author author = om.readValue(ctx.body(), Author.class);
            for(Author a: authorService.listAll()){
                if(a.checkAuthor(author)){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        authorDao.delete(a);
                        l.info("&&&\tcreator has access, deleting author "+a.toString());
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
        } catch (JsonProcessingException | SQLException | RedirectionException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong input data(400)");
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("One of NotNull params is Null(400)");
        }
    }
}
