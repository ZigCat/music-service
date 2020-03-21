package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.exception.CustomException;
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
        int id = node.get("id").asInt();
        try {
            Music music = Music.getById(node.get("music").asInt());
            User user = User.getById(node.get("user").asInt());
            return new UserMusic(id, music, user);
        } catch (SQLException e) {
            e.printStackTrace();
            l.warn(Security.serverErrorMessage);
        } catch (CustomException e){
            l.warn(Security.badRequestMessage);
        }
        throw new CustomException(Security.badRequestMessage);
    }
}
