package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.models.Author;
import com.github.zigcat.ormlite.models.Group;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.services.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class GroupDeserializer extends StdDeserializer<Group> {
    private static Logger l = LoggerFactory.getLogger(GroupDeserializer.class);

    protected GroupDeserializer(Class<?> vc) {
        super(vc);
    }

    protected GroupDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected GroupDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public GroupDeserializer(){
        super(Group.class);
    }

    @Override
    public Group deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        String creation = node.get("creationDate").asText();
        String destroy = node.get("dateOfDestroy").asText();
        if(!Security.isValidDate(creation)){
            throw new CustomException("Creation date is empty/wrong(400)");
        }
        if(!Security.isValidDate(destroy)){
            return new Group(id, name, LocalDate.parse(creation, User.dateTimeFormatter));
        }
        return new Group(id, name, LocalDate.parse(creation, User.dateTimeFormatter),
                LocalDate.parse(destroy, User.dateTimeFormatter));
    }
}
