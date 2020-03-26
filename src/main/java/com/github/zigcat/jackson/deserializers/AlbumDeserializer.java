package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.controllers.GroupController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Album;
import com.github.zigcat.ormlite.models.Author;
import com.github.zigcat.ormlite.models.Group;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.services.Security;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AlbumDeserializer extends StdDeserializer<Album> {
    protected AlbumDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AlbumDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AlbumDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public AlbumDeserializer(){
        super(Album.class);
    }

    @Override
    public Album deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        String creationDate = node.get("creationDate").asText();
        int author = node.get("author").asInt();
        int group = node.get("group").asInt();
        Author author1;
        Group group1;
        if(!Security.isValidDate(creationDate)){
            throw new CustomException("Null creationDate(401)");
        } else {
            try {
                if (author != 0) {
                    author1 = AuthorController.authorService.getById(author);
                    if (author1 == null) {
                        throw new NotFoundException("Author is not valid(404)");
                    }
                    return new Album(id, name, LocalDate.parse(creationDate, User.dateTimeFormatter),
                            author1, null);
                } else if (group != 0) {
                    group1 = GroupController.groupService.getById(group);
                    if (group1 == null) {
                        throw new NotFoundException("Group is not valid(404)");
                    }
                    return new Album(id, name, LocalDate.parse(creationDate, User.dateTimeFormatter),
                            null, group1);
                } else {
                    throw new CustomException("Wrong author/group input data(400)");
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}


