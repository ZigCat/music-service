package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.Category;

import java.io.IOException;

public class CategorySerializer extends StdSerializer<Category> {
    protected CategorySerializer(Class<Category> t) {
        super(t);
    }

    protected CategorySerializer(JavaType type) {
        super(type);
    }

    protected CategorySerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected CategorySerializer(StdSerializer<?> src) {
        super(src);
    }

    public CategorySerializer(){
        super(Category.class);
    }

    @Override
    public void serialize(Category category, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", category.getId());
        json.writeStringField("name", category.getName());
        json.writeEndObject();
    }
}
