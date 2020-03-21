package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.Genre;

import java.io.IOException;

public class GenreSerializer extends StdSerializer<Genre> {
    protected GenreSerializer(Class<Genre> t) {
        super(t);
    }

    protected GenreSerializer(JavaType type) {
        super(type);
    }

    protected GenreSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected GenreSerializer(StdSerializer<?> src) {
        super(src);
    }

    public GenreSerializer(){
        super(Genre.class);
    }

    @Override
    public void serialize(Genre genre, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", genre.getId());
        json.writeStringField("name", genre.getName());
        json.writeEndObject();
    }
}
