package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.models.Author;
import com.github.zigcat.ormlite.models.Group;
import com.github.zigcat.ormlite.models.User;
import com.github.zigcat.services.Security;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AuthorDeserializer extends StdDeserializer<Author> {
    protected AuthorDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AuthorDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AuthorDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public AuthorDeserializer(){
        super(Author.class);
    }

    @Override
    public Author deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        String birthday = node.get("birthday").asText();
        String dateOfDeath = node.get("dateOfDeath").asText();
        String description = node.get("description").asText();
        try {
            int group = node.get("group").asInt();
            Group group1 = Group.getById(group);
            if(!Security.isValidDate(birthday)){
                throw new CustomException("Creation date is empty/not valid(400)");
            }
            if(!Security.isValidDate(dateOfDeath)){
                return new Author(id, name, group1, LocalDate.parse(birthday, User.dateTimeFormatter), description);
            }
            return new Author(id, name, group1 ,description, LocalDate.parse(birthday, User.dateTimeFormatter),
                    LocalDate.parse(dateOfDeath, User.dateTimeFormatter));
        } catch (SQLException e) {
            throw new NotFoundException(Security.badRequestMessage);
        } catch (NullPointerException e){
            throw new CustomException(Security.badRequestMessage);
        }
    }
}
