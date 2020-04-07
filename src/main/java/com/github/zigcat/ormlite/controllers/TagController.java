package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.ormlite.models.Tag;
import com.github.zigcat.services.Security;
import com.github.zigcat.services.TagService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class TagController {
    public static Dao<Tag, Integer> tagDao;
    private static Logger l = LoggerFactory.getLogger(TagController.class);
    public static TagService tagService = new TagService();

    static {
        try {
            tagDao = DaoManager.createDao(DatabaseConfiguration.source, Tag.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL TAGS\t!!!");
        try {
            List<Tag> tagList = tagService.listAll();
            l.info("&&&\tgetting all tags");
            ctx.status(200);
            ctx.result(om.writeValueAsString(tagList));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(500);
            ctx.result("Generic 500 message");
            l.warn(Security.serverErrorMessage);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING TAG BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            Tag t = tagService.getById(id);
            l.info("&&&\tgetting tag by id");
            ctx.result(om.writeValueAsString(t));
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
        l.info("!!!\tCREATING TAG\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try{
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                Tag t = om.readValue(ctx.body(), Tag.class);
                l.info("&&&\tcreator has access, creating tag");
                tagDao.create(t);
                ctx.status(201);
                ctx.result(om.writeValueAsString(t));
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
        l.info("!!!\tUPDATING TAG\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try{
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                Tag t = om.readValue(ctx.body(), Tag.class);
                for(Tag tag: tagService.listAll()){
                    if(t.getId() == tag.getId()){
                        l.info("&&&\tcreator has access, updating tag");
                        tagDao.update(t);
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(t));
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
        l.info("!!!\tDELETING TAG\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try{
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                Tag t = om.readValue(ctx.body(), Tag.class);
                for(Tag tag: tagService.listAll()){
                    if(t.getId() == tag.getId()){
                        l.info("&&&\tcreator has access, deleting tag");
                        tagDao.deleteById(t.getId());
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(t));
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
}
