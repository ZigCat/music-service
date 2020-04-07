package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.zigcat.ormlite.controllers.AlbumController;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.controllers.GenreController;
import com.github.zigcat.ormlite.controllers.GroupController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.*;
import com.github.zigcat.services.Security;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class MusicDeserializer extends StdDeserializer<Music> {
    protected MusicDeserializer(Class<?> vc) {
        super(vc);
    }

    protected MusicDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected MusicDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public MusicDeserializer(){
        super(Music.class);
    }

    @Override
    public Music deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Album album1;
        Genre genre1;
        Author author1;
        Group group1;
        try {
            int id = node.get("id").asInt();
            String name = node.get("name").asText();
            int genre = node.get("genre").asInt();
            int author = node.get("author").asInt();
            int group = node.get("group").asInt();
            String creation = node.get("creationDate").asText();
            String content = node.get("content").asText();
            if(!Security.isValidDate(creation)){
                throw new CustomException("Creation date is empty/not valid(400)");
            } else {
                if (node.get("album") instanceof NullNode || node.get("album") == null) {
                    album1 = null;
                } else {
                    int album = node.get("album").asInt();
                    album1 = AlbumController.albumService.getById(album);
                }
                genre1 = GenreController.genreService.getById(genre);
                if (genre1 == null) {
                    throw new NotFoundException("Genre is not valid(400)");
                }
                if (group == 0 && author == 0) {
                    throw new CustomException("Creator is not valid, because song has 2 creators(400)");
                } else if (author != 0) {
                    author1 = AuthorController.authorService.getById(author);
                    if (author1 != null) {
                        return new Music(id, name, genre1, author1, album1, null,
                                LocalDate.parse(creation, User.dateTimeFormatter), content);
                    } else {
                        throw new NotFoundException("Author is not valid(400)");
                    }
                } else {
                    group1 = GroupController.groupService.getById(group);
                    if (group1 != null) {
                        return new Music(id, name, genre1, null, album1, group1,
                                LocalDate.parse(creation, User.dateTimeFormatter), content);
                    } else {
                        throw new NotFoundException("Group is not valid(400)");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RedirectionException("SQLException e");
        } catch (NullPointerException e){
            throw new CustomException("one of NotNull params is Null");
        }
    }
}
