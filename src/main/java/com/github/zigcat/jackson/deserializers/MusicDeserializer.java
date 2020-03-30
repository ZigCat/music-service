package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.controllers.AlbumController;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.controllers.GenreController;
import com.github.zigcat.ormlite.controllers.GroupController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.*;
import com.github.zigcat.services.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class MusicDeserializer extends StdDeserializer<Music> {
    static Logger logger = LoggerFactory.getLogger(MusicDeserializer.class);
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
        logger.info("Deserializing music object");
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = node.get("id").asInt();
        logger.info("id = " + id);
        String name = node.get("name").asText();
        logger.info("name = " + name);
        int genre = node.get("genre").asInt();
        logger.info("genre = " + genre);
        int author = node.get("author").asInt();
        logger.info("author = " + author);
        int group = node.get("group").asInt();
        logger.info("group = " + group);
        String creation = node.get("creationDate").asText();
        logger.info("creationDate = " + creation );
        String content = node.get("content").asText();
        logger.info("content = " + content);
        int album = node.get("album").asInt();
        logger.info("album = "+ album);
        Album album1;
        Genre genre1;
        Author author1;
        Group group1;
        if(!Security.isValidDate(creation)){
            logger.warn("Security Check Failed");
            throw new CustomException("Creation date is empty/not valid(400)");
        } else {
            try {
                album1 = AlbumController.albumService.getById(album);
                logger.info("Retrieved album1 from service = " + album1);
                genre1 = GenreController.genreService.getById(genre);
                logger.info("Retrieved genre1 from service = " + genre1);

                if(genre1 == null){
                    throw new NotFoundException("Genre is not valid(404)");
                }
                if(group == 0 && author == 0){
                    throw new CustomException("Creator is not valid, because song has 2 creators(400)");
                } else if(author != 0){
                    logger.info("author != 0");
                    author1 = AuthorController.authorService.getById(author);
                    logger.info("Retrieved author1 from service = " + author1);
                    if(author1 != null){
                        return new Music(id, name, genre1 ,author1, album1, null,
                                LocalDate.parse(creation, User.dateTimeFormatter), content);
                    } else {
                        throw new NotFoundException("Author is not valid(404)");
                    }
                } else {
                    group1 = GroupController.groupService.getById(group);
                    if(group1 != null){
                        return new Music(id, name, genre1 ,null, album1, group1,
                                LocalDate.parse(creation, User.dateTimeFormatter), content);
                    } else {
                        throw new NotFoundException("Group is not valid(404)");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CustomException("Bad request(400)");
            }
        }
    }
}
