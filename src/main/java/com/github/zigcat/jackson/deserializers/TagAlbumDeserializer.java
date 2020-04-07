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

import java.io.IOException;
import java.sql.SQLException;

public class TagAlbumDeserializer extends StdDeserializer<TagAlbum> {
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
        int id = node.get("id").asInt();
        Tag tag;
        Album album;
        try {
            if(node.get("tag") instanceof NullNode || node.get("tag") == null){
                tag = TagController.tagService.getById(node.get("tag").asInt());
            } else {
                throw new NotFoundException("Tag isn't valid(400)");
            }
            if(node.get("album") instanceof NullNode || node.get("album") == null){
                album = AlbumController.albumService.getById(node.get("tag").asInt());
            } else {
                throw new NotFoundException("Album isn't valid(400)");
            }
            return new TagAlbum(id, tag, album);
        } catch (SQLException e) {
            throw new RedirectionException("SQLException e");
        } catch (NullPointerException e){
            throw new CustomException("one of Not Null params is Null");
        }
    }
}
