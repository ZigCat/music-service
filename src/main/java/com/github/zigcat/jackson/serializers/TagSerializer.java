package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.Tag;
import com.github.zigcat.services.TagService;

import java.io.IOException;

public class TagSerializer extends StdSerializer<Tag> {
    protected TagSerializer(Class<Tag> t) {
        super(t);
    }

    protected TagSerializer(JavaType type) {
        super(type);
    }

    protected TagSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected TagSerializer(StdSerializer<?> src) {
        super(src);
    }

    public TagSerializer(){
        super(Tag.class);
    }

    @Override
    public void serialize(Tag tag, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", tag.getId());
        json.writeStringField("name", tag.getName());
        json.writeObjectField("tag", tag.getCategory());
        json.writeEndObject();
    }
}
