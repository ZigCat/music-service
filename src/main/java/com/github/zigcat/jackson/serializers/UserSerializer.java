package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    protected UserSerializer(Class<User> t) {
        super(t);
    }

    protected UserSerializer(JavaType type) {
        super(type);
    }

    protected UserSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected UserSerializer(StdSerializer<?> src) {
        super(src);
    }

    public UserSerializer(){
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", user.getId());
        json.writeStringField("name", user.getName());
        json.writeStringField("surname", user.getSurname());
        json.writeStringField("role", user.getRole().toString());
        json.writeEndObject();
    }
}
