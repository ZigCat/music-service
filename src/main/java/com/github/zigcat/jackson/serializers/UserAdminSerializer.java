package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.User;

import java.io.IOException;

public class UserAdminSerializer extends StdSerializer<User> {

    protected UserAdminSerializer(Class<User> t) {
        super(t);
    }

    protected UserAdminSerializer(JavaType type) {
        super(type);
    }

    protected UserAdminSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected UserAdminSerializer(StdSerializer<?> src) {
        super(src);
    }

    public UserAdminSerializer(){
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", user.getId());
        json.writeStringField("name", user.getName());
        json.writeStringField("surname", user.getSurname());
        json.writeStringField("email", user.getEmail());
        json.writeStringField("registration_date", user.getRegDate());
        json.writeStringField("role", user.getRole().toString());
        json.writeEndObject();
    }
}
