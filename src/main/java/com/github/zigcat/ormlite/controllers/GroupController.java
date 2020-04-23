package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Group;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.services.GroupService;
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

public class GroupController {
    public static Dao<Group, Integer> groupDao;
    private static Logger l = LoggerFactory.getLogger(GroupController.class);
    public static GroupService groupService = new GroupService();
    private static PaginationService paginationService = new PaginationService();

    static {
        try {
            groupDao = DaoManager.createDao(DatabaseConfiguration.source, Group.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL GROUPS\t!!!");
        Map map = ctx.queryParamMap();
        long page;
        try {
            if(map.containsKey("page")){
                page = Long.parseLong(ctx.queryParam("page"));
            } else {
                page = 1;
            }
            ctx.result(om.writeValueAsString(paginationService.pagitation(groupDao, page, 10)));
            ctx.status(200);
            l.info("&&&\tgetting all groups");
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

    public static void getById(Context ctx, ObjectMapper om) {
        l.info("!!!\tGETTING GROUP BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            for(Group g: groupService.listAll()){
                l.info("Iterating over "+g.toString());
                if(g.getId() == id){
                    ctx.result(om.writeValueAsString(g));
                    ctx.status(200);
                    l.info("&&&\tgetting info about "+g.toString());
                    break;
                }
            }
        } catch (SQLException | JsonProcessingException e) {
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
            e.printStackTrace();
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING GROUP\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Group group = om.readValue(ctx.body(), Group.class);
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreator has access, creating group "+group.toString());
                groupDao.create(group);
                ctx.result(om.writeValueAsString(group));
                ctx.status(201);
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
        l.info("!!!\tUPDATING GROUP\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Group updGroup = om.readValue(ctx.body(), Group.class);
            for(Group g: groupService.listAll()){
                l.info("Iterating over "+g.toString());
                if(g.getId() == updGroup.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, updating group "+g.toString());
                        groupDao.update(updGroup);
                        ctx.result(om.writeValueAsString(updGroup));
                        ctx.status(200);
                        l.info("&&&\tgroup updated to "+updGroup.toString());
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
        l.info("!!!\tDELETING GROUP\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map map = ctx.queryParamMap();
        try {
            if(map.containsKey("id")){
                int id = Integer.parseInt(ctx.queryParam("id"));
                for(Group g: groupService.listAll()){
                    l.info("Iterating over "+g.toString());
                    if(g.getId() == id){
                        if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                            l.info("&&&\tcreator has access, deleting "+g.toString());
                            groupDao.deleteById(id);
                            ctx.result(om.writeValueAsString(g));
                            ctx.status(200);
                        }
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
        l.info("!!!\tQUERY DONE\t!!!");
    }
}
