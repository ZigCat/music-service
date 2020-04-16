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
//Other Entity's ObjectMapper
        ObjectMapper om = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
        //serializers
        sm.addSerializer(Album.class, new AlbumSerializer());
        sm.addSerializer(AuthorGroup.class, new AuthorGroupSerializer());
        sm.addSerializer(Author.class, new AuthorSerializer());
        sm.addSerializer(CategoryGenre.class, new CategoryGenreSerializer());
        sm.addSerializer(Category.class, new CategorySerializer());
        sm.addSerializer(Genre.class, new GenreSerializer());
        sm.addSerializer(Group.class, new GroupSerializer());
        sm.addSerializer(Music.class, new MusicSerializer());
        sm.addSerializer(TagAlbum.class, new TagAlbumSerializer());
        sm.addSerializer(Tag.class, new TagSerializer());
        sm.addSerializer(UserMusic.class, new UserMusicSerializer());
        //deserializers
        sm.addDeserializer(Album.class, new AlbumDeserializer());
        sm.addDeserializer(AuthorGroup.class, new AuthorGroupDeserializer());
        sm.addDeserializer(Author.class, new AuthorDeserializer());
        sm.addDeserializer(CategoryGenre.class, new CategoryGenreDeserializer());
        sm.addDeserializer(Category.class, new CategoryDeserializer());
        sm.addDeserializer(Genre.class, new GenreDeserializer());
        sm.addDeserializer(Group.class, new GroupDeserializer());
        sm.addDeserializer(Music.class, new MusicDeserializer());
        sm.addDeserializer(TagAlbum.class, new TagAlbumDeserializer());
        sm.addDeserializer(Tag.class, new TagDeserializer());
        sm.addDeserializer(UserMusic.class, new UserMusicDeserializer());
        sm.addSerializer(User.class, new UserSerializer());
        sm.addDeserializer(User.class, new UserDeserializer());
        //registering module
        om.registerModule(sm);
//starting app
        Javalin app = Javalin.create();
        app.config = new JavalinConfig().enableDevLogging();
        app.start(34567);
//implementing app's methods
//user CRUD
        app.get("user/", ctx -> UserController.getAll(ctx, omUser, omUserAdmin));
        app.get("user/:id", ctx -> UserController.getById(ctx, omUser, omUserAdmin));
        app.post("user/", ctx -> UserController.create(ctx, omUser));
        app.patch("user/", ctx -> UserController.update(ctx, omUser));
        app.delete("user/", ctx -> UserController.delete(ctx, omUser));
        app.exception(IllegalArgumentException.class, (e, ctx) ->{ctx.status(403);})
                .error(403, ctx -> ctx.result("Generic 403 message"));
//author CRUD
        app.get("author/", ctx -> AuthorController.getAll(ctx, om));
        app.get("author/:id", ctx -> AuthorController.getById(ctx, om));
        app.post("author/", ctx -> AuthorController.create(ctx, om));
        app.patch("author/", ctx -> AuthorController.update(ctx, om));
        app.delete("author/", ctx -> AuthorController.delete(ctx, om));
//album CRUD
        app.get("album/", ctx -> AlbumController.getAll(ctx, om));
        app.get("album/:id", ctx -> AlbumController.getById(ctx, om));
        app.post("album/", ctx -> AlbumController.create(ctx, om));
        app.patch("album/", ctx -> AlbumController.update(ctx, om));
        app.delete("album/", ctx -> AlbumController.delete(ctx, om));
//group CRUD
        app.get("group/", ctx -> GroupController.getAll(ctx, om));
        app.get("group/:id", ctx -> GroupController.getById(ctx, om));
        app.post("group/", ctx -> GroupController.create(ctx, om));
        app.patch("group/", ctx -> GroupController.update(ctx, om));
        app.delete("group/", ctx -> GroupController.delete(ctx, om));
//genre CRUD
        app.get("genre/", ctx -> GenreController.getAll(ctx, om));
        app.get("genre/:id", ctx -> GenreController.getById(ctx, om));
        app.post("genre/", ctx -> GenreController.create(ctx, om));
        app.patch("genre/", ctx -> GenreController.update(ctx, om));
        app.delete("genre/", ctx -> GenreController.delete(ctx, om));
//music CRUD
        app.get("music/", ctx -> MusicController.getAll(ctx, om));
        app.get("music/:id", ctx -> MusicController.getById(ctx, om));
        app.post("music/", ctx -> MusicController.create(ctx, om));
        app.patch("music/", ctx -> MusicController.update(ctx, om));
        app.delete("music/", ctx -> MusicController.delete(ctx, om));
//userMusic CRUD
        app.get("audio/", ctx -> UserMusicController.getAll(ctx, om));
        app.get("audio/:id", ctx -> UserMusicController.getById(ctx, om));
        app.post("audio/", ctx -> UserMusicController.addMusic(ctx, om));
        app.delete("audio/", ctx -> UserMusicController.delete(ctx, om));
//search Music
        app.get("search/", ctx -> MusicController.advancedSearch(ctx, om));
//authorGroup CRUD
        app.get("author/group/", ctx -> AuthorGroupController.getAll(ctx, om));
        app.get("author/group/:id", ctx -> AuthorGroupController.getById(ctx, om));
        app.post("author/group/", ctx -> AuthorGroupController.create(ctx, om));
        app.patch("author/group/", ctx -> AuthorGroupController.update(ctx, om));
        app.delete("author/group/", ctx -> AuthorGroupController.delete(ctx, om));
//Tag CRUD
        app.get("tag/", ctx -> TagController.getAll(ctx, om));
        app.get("tag/:id", ctx -> TagController.getById(ctx, om));
        app.post("tag/", ctx -> TagController.create(ctx, om));
        app.patch("tag/", ctx -> TagController.update(ctx, om));
        app.delete("tag/", ctx -> TagController.delete(ctx, om));
//TagAlbum CRUD
        app.get("albumtag/", ctx -> TagAlbumController.getAll(ctx, om));
        app.get("albumtag/:id", ctx -> TagAlbumController.getById(ctx, om));
        app.post("albumtag/", ctx -> TagAlbumController.create(ctx, om));
        app.patch("albumtag/", ctx -> TagAlbumController.update(ctx, om));
        app.delete("albumtag/", ctx -> TagAlbumController.delete(ctx, om));
//Category CRUD
        app.get("category/", ctx -> CategoryController.getAll(ctx, om));
        app.get("category/:id", ctx -> CategoryController.getById(ctx, om));
        app.post("category/", ctx -> CategoryController.create(ctx, om));
        app.patch("category/", ctx -> CategoryController.update(ctx, om));
        app.delete("category/", ctx -> CategoryController.delete(ctx, om));
        app.get("select/", ctx -> MusicController.selection(ctx, om));
//CategoryGenre CRUD
        app.get("genre/category/", ctx -> CategoryGenreController.getAll(ctx, om));
        app.get("genre/category/:id", ctx -> CategoryGenreController.getById(ctx, om));
        app.post("genre/category/", ctx -> CategoryGenreController.create(ctx, om));
        app.patch("genre/category/", ctx -> CategoryGenreController.update(ctx, om));
        app.delete("genre/category/", ctx -> CategoryGenreController.delete(ctx, om));
    }
}
