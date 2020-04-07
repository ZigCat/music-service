package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.zigcat.ormlite.controllers.CategoryController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Category;
import com.github.zigcat.ormlite.models.Tag;

import java.io.IOException;
import java.sql.SQLException;

public class TagDeserializer extends StdDeserializer<Tag> {
    protected TagDeserializer(Class<?> vc) {
        super(vc);
    }

    protected TagDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected TagDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public TagDeserializer(){
        super(Tag.class);
    }

    @Override
    public Tag deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Category category;
        try {
            int id = node.get("id").asInt();
            String name = node.get("name").asText();
            if(node.get("category") instanceof NullNode || node.get("category") == null){
                throw new NotFoundException("Category isn't valid(400)");
            } else {
                category = CategoryController.categoryService.getById(node.get("category").asInt());
            }
            return new Tag(id, name, category);
        } catch (SQLException e){
            throw new RedirectionException("SQLException e");
        } catch (NullPointerException e){
            throw new CustomException("one of NotNull params is Null");
        }
    }
}
