package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.CategoryGenre;
import com.github.zigcat.services.CategoryGenreService;

import java.io.IOException;

public class CategoryGenreSerializer extends StdSerializer<CategoryGenre> {
    protected CategoryGenreSerializer(Class<CategoryGenre> t) {
        super(t);
    }

    protected CategoryGenreSerializer(JavaType type) {
        super(type);
    }

    protected CategoryGenreSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected CategoryGenreSerializer(StdSerializer<?> src) {
        super(src);
    }

    public CategoryGenreSerializer(){
        super(CategoryGenre.class);
    }

    @Override
    public void serialize(CategoryGenre cg, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", cg.getId());
        json.writeObjectField("category", cg.getCategory());
        json.writeObjectField("genre", cg.getGenre());
        json.writeEndObject();
    }
}
