package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.TagAlbum;

import java.io.IOException;

public class TagAlbumSerializer extends StdSerializer<TagAlbum> {
    protected TagAlbumSerializer(Class<TagAlbum> t) {
        super(t);
    }

    protected TagAlbumSerializer(JavaType type) {
        super(type);
    }

    protected TagAlbumSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected TagAlbumSerializer(StdSerializer<?> src) {
        super(src);
    }

    public TagAlbumSerializer(){
        super(TagAlbum.class);
    }

    @Override
    public void serialize(TagAlbum ta, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", ta.getId());
        json.writeObjectField("tag", ta.getTag());
        json.writeObjectField("album", ta.getAlbum());
        json.writeEndObject();
    }
}
