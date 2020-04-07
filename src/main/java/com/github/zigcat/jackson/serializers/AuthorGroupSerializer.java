package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.AuthorGroup;

import java.io.IOException;

public class AuthorGroupSerializer extends StdSerializer<AuthorGroup> {
    protected AuthorGroupSerializer(Class<AuthorGroup> t) {
        super(t);
    }

    protected AuthorGroupSerializer(JavaType type) {
        super(type);
    }

    protected AuthorGroupSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected AuthorGroupSerializer(StdSerializer<?> src) {
        super(src);
    }

    public AuthorGroupSerializer(){
        super(AuthorGroup.class);
    }

    @Override
    public void serialize(AuthorGroup ag, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", ag.getId());
        json.writeObjectField("author", ag.getAuthor());
        json.writeObjectField("group", ag.getGroup());
        json.writeEndObject();
    }
}
