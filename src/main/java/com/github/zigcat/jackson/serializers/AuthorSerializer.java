package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.Author;

import java.io.IOException;

public class AuthorSerializer extends StdSerializer<Author> {
    protected AuthorSerializer(Class<Author> t) {
        super(t);
    }

    protected AuthorSerializer(JavaType type) {
        super(type);
    }

    protected AuthorSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected AuthorSerializer(StdSerializer<?> src) {
        super(src);
    }

    public AuthorSerializer(){
        super(Author.class);
    }

    @Override
    public void serialize(Author author, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", author.getId());
        json.writeStringField("name", author.getName());
        json.writeObjectField("group", author.getGroup());
        json.writeStringField("birthday", author.getBirthday());
        json.writeStringField("dateOfDeath", author.getDateOfDeath());
        json.writeStringField("description", author.getDescription());
        json.writeEndObject();
    }
}
