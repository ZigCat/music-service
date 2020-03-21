package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.UserMusic;

import java.io.IOException;

public class UserMusicSerializer extends StdSerializer<UserMusic> {
    protected UserMusicSerializer(Class<UserMusic> t) {
        super(t);
    }

    protected UserMusicSerializer(JavaType type) {
        super(type);
    }

    protected UserMusicSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected UserMusicSerializer(StdSerializer<?> src) {
        super(src);
    }

    public UserMusicSerializer(){
        super(UserMusic.class);
    }

    @Override
    public void serialize(UserMusic userMusic, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", userMusic.getId());
        json.writeObjectField("user", userMusic.getUser());
        json.writeObjectField("music", userMusic.getMusic());
        json.writeEndObject();
    }
}
