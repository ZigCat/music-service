package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.models.Genre;

import java.io.IOException;

public class GenreDeserializer extends StdDeserializer<Genre> {
    protected GenreDeserializer(Class<?> vc) {
        super(vc);
    }

    protected GenreDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected GenreDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public GenreDeserializer(){
        super(Genre.class);
    }

    @Override
    public Genre deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        return new Genre(id, name);
    }
}
