package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.Album;

import java.io.IOException;

public class AlbumSerializer extends StdSerializer<Album> {
    protected AlbumSerializer(Class<Album> t) {
        super(t);
    }

    protected AlbumSerializer(JavaType type) {
        super(type);
    }

    protected AlbumSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected AlbumSerializer(StdSerializer<?> src) {
        super(src);
    }

    public AlbumSerializer(){
        super(Album.class);
    }

    @Override
    public void serialize(Album album, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", album.getId());
        json.writeStringField("name", album.getName());
        json.writeStringField("creationDate", album.getCreationDate());
        json.writeObjectField("author", album.getAuthor());
        json.writeObjectField("group", album.getGroup());
        json.writeEndObject();
    }
}
