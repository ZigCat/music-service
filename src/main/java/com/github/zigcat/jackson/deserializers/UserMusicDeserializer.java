package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.zigcat.ormlite.controllers.MusicController;
import com.github.zigcat.ormlite.controllers.UserController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Music;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.ormlite.models.UserMusic;
import com.github.zigcat.services.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class UserMusicDeserializer extends StdDeserializer<UserMusic> {

    private static Logger l = LoggerFactory.getLogger(UserMusicDeserializer.class);

    protected UserMusicDeserializer(Class<?> vc) {
        super(vc);
    }

    protected UserMusicDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected UserMusicDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public UserMusicDeserializer(){
        super(UserMusic.class);
    }

    @Override
    public UserMusic deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        User user;
        Music music;
        try {
            int id = node.get("id").asInt();
            if(node.get("music") instanceof NullNode || node.get("music") == null){
                throw new NotFoundException("music isn't valid(400)");
            } else {
                music = MusicController.musicService.getById(node.get("music").asInt());
            }
            if(node.get("user") instanceof NullNode || node.get("user") == null){
                throw new NotFoundException("user isn't valid(400)");
            } else {
                user = UserController.userService.getById(node.get("user").asInt());
            }
            return new UserMusic(id, music, user);
        } catch (SQLException e) {
            throw new RedirectionException("SQLException e");
        } catch (NullPointerException e){
            throw new CustomException("one of NotNull params is Null");
        }
    }
}
