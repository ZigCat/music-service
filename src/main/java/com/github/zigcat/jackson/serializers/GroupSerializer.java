package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.models.Group;

import java.io.IOException;

public class GroupSerializer extends StdSerializer<Group> {
    protected GroupSerializer(Class<Group> t) {
        super(t);
    }

    protected GroupSerializer(JavaType type) {
        super(type);
    }

    protected GroupSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected GroupSerializer(StdSerializer<?> src) {
        super(src);
    }

    public GroupSerializer(){
        super(Group.class);
    }

    @Override
    public void serialize(Group group, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        json.writeStartObject();
        json.writeNumberField("id", group.getId());
        json.writeStringField("name", group.getName());
        json.writeStringField("creationDate", group.getCreationDate());
        json.writeStringField("dateOfDestroy", group.getDateOfDestroy());
        json.writeEndObject();
    }
}
