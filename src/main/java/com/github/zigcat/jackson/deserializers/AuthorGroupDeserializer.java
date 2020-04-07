package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.controllers.GroupController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Author;
import com.github.zigcat.ormlite.models.AuthorGroup;
import com.github.zigcat.ormlite.models.Group;
import com.github.zigcat.services.AuthorService;

import java.io.IOException;
import java.sql.SQLException;

public class AuthorGroupDeserializer extends StdDeserializer<AuthorGroup> {
    protected AuthorGroupDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AuthorGroupDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AuthorGroupDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public AuthorGroupDeserializer(){
        super(AuthorGroup.class);
    }

    @Override
    public AuthorGroup deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Author author;
        Group group;
        try{
            int id = node.get("id").asInt();
            if(node.get("author") instanceof NullNode || node.get("author") == null){
                throw new NotFoundException("Author is not valid(400)");
            } else {
                author = AuthorController.authorService.getById(node.get("author").asInt());
            }
            if(node.get("group") instanceof NullNode || node.get("group") == null){
                throw new NotFoundException("Group is not valid(400)");
            } else {
                group = GroupController.groupService.getById(node.get("group").asInt());
            }
            return new AuthorGroup(id, author, group);
        } catch(SQLException e){
            throw new RedirectionException("SQLException");
        } catch (NullPointerException e){
            throw new CustomException("one of NotNull params is Null");
        }
    }
}
