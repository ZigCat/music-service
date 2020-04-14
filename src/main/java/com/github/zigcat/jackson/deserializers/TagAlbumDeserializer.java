package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.zigcat.ormlite.controllers.AlbumController;
import com.github.zigcat.ormlite.controllers.TagController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Album;
import com.github.zigcat.ormlite.models.Tag;
import com.github.zigcat.ormlite.models.TagAlbum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class TagAlbumDeserializer extends StdDeserializer<TagAlbum> {
    private static Logger l = LoggerFactory.getLogger(TagAlbumDeserializer.class);
    protected TagAlbumDeserializer(Class<?> vc) {
        super(vc);
    }

    protected TagAlbumDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected TagAlbumDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public TagAlbumDeserializer(){
        super(TagAlbum.class);
    }

    @Override
    public TagAlbum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        l.info("@@@\tdeserializing tagalbum");
        int id = node.get("id").asInt();
        Tag tag;
        Album album;
        try {
            l.info("checking tag");
            if(node.get("tag") instanceof NullNode || node.get("tag") == null){
                l.info("wrong tag");
                throw new NotFoundException("Tag isn't valid(400)");
            } else {
                tag = TagController.tagService.getById(node.get("tag").asInt());
            }
            l.info("checking album");
            if(node.get("album") instanceof NullNode || node.get("album") == null){
                l.info("wrong album");
                throw new NotFoundException("Album isn't valid(400)");
            } else {
                album = AlbumController.albumService.getById(node.get("album").asInt());
            }
            return new TagAlbum(id, tag, album);
        } catch (SQLException e) {
            throw new RedirectionException("SQLException e");
        } catch (NullPointerException e){
            throw new CustomException("one of Not Null params is Null");
        }
    }
}
