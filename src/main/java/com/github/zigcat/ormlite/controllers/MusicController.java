package com.github.zigcat.ormlite.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zigcat.DatabaseConfiguration;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.*;
import com.github.zigcat.services.MusicService;
import com.github.zigcat.services.Security;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicController {
    public static Dao<Music, Integer> musicDao;
    private static Logger l = LoggerFactory.getLogger(MusicController.class);
    public static MusicService musicService = new MusicService();

    static {
        try {
            musicDao = DaoManager.createDao(DatabaseConfiguration.source, Music.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAll(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING ALL MUSIC\t!!!");
        try {
            List<Music> musicList = musicService.listAll();
            l.info("&&&\tgetting all music");
            ctx.status(200);
            ctx.result(om.writeValueAsString(musicList));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void getById(Context ctx, ObjectMapper om){
        l.info("!!!\tGETTING MUSIC BY ID\t!!!");
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            for(Music m: musicService.listAll()){
                l.info("Iterating over "+m.toString());
                if(m.getId() == id){
                    l.info("&&&\tgetting music file:"+m.toString());
                    ctx.status(200);
                    ctx.result(new FileInputStream(new File(m.getContent())));
                    break;
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.result("Generic 500 message");
            ctx.status(500);
        }
    }

    public static void create(Context ctx, ObjectMapper om){
        l.info("!!!\tCREATING NEW MUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Music music = om.readValue(ctx.body(), Music.class);
            if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                l.info("&&&\tcreator has access, creating music "+music.toString());
                ctx.status(201);
                ctx.result(om.writeValueAsString(music));
                musicDao.create(music);
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
        l.info("!!!\tUPDATING MUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Music music = om.readValue(ctx.body(), Music.class);
            for(Music m: musicService.listAll()){
                if(m.getId() == music.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, updating music "+m.toString());
                        musicDao.update(music);
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(music));
                        l.info("&&&\tupdated to "+music.toString());
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
        l.info("!!!\tDELETING MUSIC\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        try {
            Music music = om.readValue(ctx.body(), Music.class);
            for(Music m: musicService.listAll()){
                if(m.getId() == music.getId()){
                    if(Security.authorize(login, password).getRole().equals(Role.ADMIN)){
                        l.info("&&&\tcreator has access, deleting music "+m.toString());
                        ctx.status(200);
                        ctx.result(om.writeValueAsString(m));
                        musicDao.deleteById(m.getId());
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

    public static void advancedSearch(Context ctx, ObjectMapper om){
        l.info("!!!\t SEARCHING MUSIC\t!!!");
        Map map = ctx.queryParamMap();
        ArrayList<Music> music = new ArrayList<>();
        String name = null;
        Author author = null;
        Group group = null;
        Genre genre = null;
        try {
            l.info("checking queryParam");
            if(map.containsKey("author") && map.containsKey("group")
                    && map.containsKey("genre") && map.containsKey("name")){
                throw new CustomException("queryParam is empty");
            }
            if(map.containsKey("author") && !map.containsKey("group")){
                author = AuthorController.authorService.getById(Integer.parseInt(ctx.queryParam("author")));
                l.info("^author^ "+author.toString());
            } else if(map.containsKey("author") && map.containsKey("group")){
                throw new CustomException("Music can not contains Author and Group simultaneous");
            } else if(!map.containsKey("author") && map.containsKey("group")){
                group = GroupController.groupService.getById(Integer.parseInt(ctx.queryParam("group")));
                l.info("^group^ "+group.toString());
            }

            if(map.containsKey("name")){
                name = ctx.queryParam("name");
                l.info("^name^ "+name);
            }
            if(map.containsKey("genre")){
                genre = GenreController.genreService.getById(Integer.parseInt(ctx.queryParam("genre")));
                l.info("^genre^ "+genre.toString());
            }
            if(name != null){
                l.info("&&&\tdoing full search");
                for(Music m: musicDao.queryForAll()){
                    if(m.getName().toLowerCase().contains(name.toLowerCase())){
                        music.add(m);
                    }
                }
                if(author != null){
                    for(Music m:music){
                        if(m.getAuthor().getId() != author.getId()){
                            music.remove(m);
                        }
                    }
                } else if(group != null){
                    for(Music m:music){
                        if(m.getGroup().getId() != group.getId()){
                            music.remove(m);
                        }
                    }
                }
                if(genre != null){
                    for(Music m:music){
                        if(m.getGenre().getId() != genre.getId()){
                            music.remove(m);
                        }
                    }
                }
            } else if(author != null){
                l.info("&&&\tsearch by author");
                for(Music m:musicDao.queryForAll()){
                    if(m.getAuthor() != null && m.getAuthor().getId() == author.getId()){
                        music.add(m);
                    }
                }
            } else if(group != null){
                l.info("&&&\tsearch by group");
                for(Music m:musicDao.queryForAll()){
                    l.info("Iterating over "+m.toString());
                    if(m.getGroup() != null && m.getGroup().getId() == group.getId()){
                        music.add(m);
                    }
                }
            } else if(genre != null){
                l.info("&&&\tsearch by genre");
                for(Music m:musicDao.queryForAll()){
                    if(m.getGenre().getId() == genre.getId()){
                        music.add(m);
                    }
                }
            }
            l.info("&&&\tgetting result");
            ctx.result(om.writeValueAsString(music));
            ctx.status(200);
        } catch (SQLException | JsonProcessingException | RedirectionException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Generic 400 message");
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong input data(400)");
        } catch (NumberFormatException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong query param(400 message)");
        }
        l.info("!!!\tQUERY DONE\t!!!");
    }

    public static void selection(Context ctx, ObjectMapper om){
        l.info("!!!\tMUSIC SELECTION\t!!!");
        String login = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        Map queryMap = ctx.queryParamMap();
        int type;
        User user = Security.authorize(login, password);
        try {
           l.info("&&&\tchecking query param");
           if(queryMap.containsKey("search_type")){
               type = Integer.parseInt(ctx.queryParam("search_type"));
           } else {
               l.warn("query param is null");
               throw new CustomException("Main query param is Null(400)");
           }
           switch(type){
               case 1:
                   l.info("&&&\tsearch type = 1");
                   ArrayList<Integer> tags = new ArrayList<>();
                   ArrayList<Integer> tagList;
                   int max, maxVar = 0;
                   tagList = new ArrayList<>();
                   for(Music m: UserMusicController.umService.getMusic(user)){
                       tagList.addAll(TagAlbumController.taService.getByAlbum(m.getAlbum()));
                   }
                   for(Integer i: tagList){
                       if(!tags.contains(i)){
                           tags.add(i);
                       }
                   }
                   int[] nums = new int[tags.size()];
                   for(int i=0;i<nums.length;i++){
                       nums[i] = 0;
                   }
                   for(Integer integer : tagList) {
                       nums[tags.indexOf(integer)] += 1;
                   }
                   l.info("&&&\tchoosing tag");
                   max = nums[0];
                   for(int i=0;i<nums.length;i++){
                       for (int num : nums) {
                           if (nums[i] > num && nums[i] > maxVar) {
                               maxVar = nums[i];
                               max = tags.get(i);
                               l.info("max = "+max);
                           } else if(nums[i] < num && num > maxVar){
                               maxVar = num;
                               max = tags.get(i);
                               l.info("max = "+max);
                           }
                       }
                   }
                   l.info("&&&\tgetting search tag by id "+max);
                   Tag tag = TagController.tagService.getById(max);
                   l.info("&&&\tgetting by "+tag.toString());
                   ctx.status(200);
                   ctx.result(om.writeValueAsString(TagAlbumController.taService.getByTag(tag)));
                   break;
               case 2:
                   l.info("&&&\tsearch type = "+type);
                   if(queryMap.containsKey("genre")){
                       l.info("checkpoint");
                   } else {
                       throw new NullPointerException("wrong query param");
                   }
                   break;
           }
        } catch (SQLException | JsonProcessingException | RedirectionException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
            ctx.status(500);
            ctx.result("Generic 500 message");
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Generic 400 message");
        } catch (NotFoundException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong input data(400)");
        } catch (NumberFormatException | NullPointerException e){
            l.warn(Security.badRequestMessage);
            ctx.status(400);
            ctx.result("Wrong query param(400 message)");
        }
        l.info("!!!\tQUERY DONE\t!!!");    }
}
