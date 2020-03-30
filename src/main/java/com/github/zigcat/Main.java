package com.github.zigcat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.zigcat.jackson.deserializers.*;
import com.github.zigcat.jackson.serializers.*;
import com.github.zigcat.ormlite.controllers.*;
import com.github.zigcat.ormlite.models.*;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public class Main {
    public static void main(String[] args) {
//Simple User ObjectMapper
        ObjectMapper omUser = new ObjectMapper();
        SimpleModule smUser = new SimpleModule();
        smUser.addSerializer(User.class, new UserSerializer());
        smUser.addDeserializer(User.class, new UserDeserializer());
        omUser.registerModule(smUser);
//Admin User ObjectMapper
        ObjectMapper omUserAdmin = new ObjectMapper();
        SimpleModule smUserAdmin = new SimpleModule();
        smUserAdmin.addDeserializer(User.class, new UserDeserializer());
        smUserAdmin.addSerializer(User.class, new UserAdminSerializer());
        omUserAdmin.registerModule(smUserAdmin);
//Author ObjectMapper
        ObjectMapper omAuthor = new ObjectMapper();
        SimpleModule smAuthor = new SimpleModule();
        smAuthor.addSerializer(Author.class, new AuthorSerializer());
        smAuthor.addDeserializer(Author.class, new AuthorDeserializer());
        omAuthor.registerModule(smAuthor);
//Album ObjectMapper
        ObjectMapper omAlbum = new ObjectMapper();
        SimpleModule smAlbum = new SimpleModule();
        smAlbum.addDeserializer(Album.class, new AlbumDeserializer());
        smAlbum.addSerializer(Album.class, new AlbumSerializer());
        omAlbum.registerModule(smAlbum);
//Group ObjectMapper
        ObjectMapper omGroup = new ObjectMapper();
        SimpleModule smGroup = new SimpleModule();
        smGroup.addSerializer(Group.class, new GroupSerializer());
        smGroup.addDeserializer(Group.class, new GroupDeserializer());
        omGroup.registerModule(smGroup);
//Genre ObjectMapper
        ObjectMapper omGenre = new ObjectMapper();
        SimpleModule smGenre = new SimpleModule();
        smGenre.addDeserializer(Genre.class, new GenreDeserializer());
        smGenre.addSerializer(Genre.class, new GenreSerializer());
        omGenre.registerModule(smGenre);
//Music ObjectMapper
        ObjectMapper omMusic = new ObjectMapper();
        SimpleModule smMusic = new SimpleModule();
        smMusic.addSerializer(Music.class, new MusicSerializer());
        smMusic.addDeserializer(Music.class, new MusicDeserializer());
        smMusic.addSerializer(Author.class, new AuthorSerializer());
        omMusic.registerModule(smMusic);
//UserMusic ObjectMapper
        ObjectMapper omUm = new ObjectMapper();
        SimpleModule smUm = new SimpleModule();
        smUm.addSerializer(User.class, new UserSerializer());
        smUm.addSerializer(Music.class, new MusicSerializer());
        smUm.addSerializer(UserMusic.class, new UserMusicSerializer());
        smUm.addDeserializer(UserMusic.class, new UserMusicDeserializer());
        omUm.registerModule(smUm);
//starting app
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.enableDevLogging();
            javalinConfig.defaultContentType = "application/json";
            javalinConfig.prefer405over404 = true;
        });
        app.start(34567);
//implementing app's methods
//user CRUD
        app.get("user/", ctx -> UserController.getAll(ctx, omUser, omUserAdmin));
        app.get("user/:id", ctx -> UserController.getById(ctx, omUser, omUserAdmin));
        app.post("user/", ctx -> UserController.create(ctx, omUser));
        app.patch("user/", ctx -> UserController.update(ctx, omUser));
        app.delete("user/", ctx -> UserController.delete(ctx, omUser));
        //app.exception(IllegalArgumentException.class, (e, ctx) ->{ctx.status(403);})
                //.error(403, ctx -> {ctx.result("Generic 403 message");});
//author CRUD
        app.get("author/", ctx -> AuthorController.getAll(ctx, omAuthor));
        app.get("author/:id", ctx -> AuthorController.getById(ctx, omAuthor));
        app.post("author/", ctx -> AuthorController.create(ctx, omAuthor));
        app.patch("author/", ctx -> AuthorController.update(ctx, omAuthor));
        app.delete("author/", ctx -> AuthorController.delete(ctx, omAuthor));
//album CRUD
        app.get("album/", ctx -> AlbumController.getAll(ctx, omAlbum));
        app.get("album/:id", ctx -> AlbumController.getById(ctx, omAlbum));
        app.post("album/", ctx -> AlbumController.create(ctx, omAlbum));
        app.patch("album/", ctx -> AlbumController.update(ctx, omAlbum));
        app.delete("album/", ctx -> AlbumController.delete(ctx, omAlbum));
//group CRUD
        app.get("group/", ctx -> GroupController.getAll(ctx, omGroup));
        app.get("group/:id", ctx -> GroupController.getById(ctx, omGroup));
        app.post("group/", ctx -> GroupController.create(ctx, omGroup));
        app.patch("group/", ctx -> GroupController.update(ctx, omGroup));
        app.delete("group/", ctx -> GroupController.delete(ctx, omGroup));
//genre CRUD
        app.get("genre/", ctx -> GenreController.getAll(ctx, omGenre));
        app.get("genre/:id", ctx -> GenreController.getById(ctx, omGenre));
        app.post("genre/", ctx -> GenreController.create(ctx, omGenre));
        app.patch("genre/", ctx -> GenreController.update(ctx, omGenre));
        app.delete("genre/", ctx -> GenreController.delete(ctx, omGenre));
//music CRUD
        app.get("music/", ctx -> MusicController.getAll(ctx, omMusic));
        app.get("music/:id", ctx -> MusicController.getById(ctx, omMusic));
        app.post("music/", ctx -> MusicController.create(ctx, omMusic));
        app.patch("music/", ctx -> MusicController.update(ctx, omMusic));
        app.delete("music/", ctx -> MusicController.delete(ctx, omMusic));
//userMusic CRUD
        app.get("audio/", ctx -> UserMusicController.getAll(ctx, omUm));
        app.get("audio/:id", ctx -> UserMusicController.getById(ctx, omUm));
        app.post("audio/", ctx -> UserMusicController.addMusic(ctx, omUm));
        app.delete("audio/", ctx -> UserMusicController.delete(ctx, omUm));
//search Music
        app.get("search/", ctx -> MusicController.advancedSearch(ctx, omMusic));
    }
}
