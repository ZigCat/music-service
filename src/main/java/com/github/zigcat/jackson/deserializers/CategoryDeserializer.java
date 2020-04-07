package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.models.Category;

import java.io.IOException;

public class CategoryDeserializer extends StdDeserializer<Category> {
    protected CategoryDeserializer(Class<?> vc) {
        super(vc);
    }

    protected CategoryDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected CategoryDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public CategoryDeserializer(){
        super(Category.class);
    }

    @Override
    public Category deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try {
            int id = node.get("id").asInt();
            String name = node.get("name").asText();
            return new Category(id, name);
        } catch (NullPointerException e){
            throw new CustomException("one of NotNull params is Null");
        }
    }
}
