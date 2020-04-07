package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.zigcat.ormlite.controllers.CategoryController;
import com.github.zigcat.ormlite.controllers.GenreController;
import com.github.zigcat.ormlite.exception.CustomException;
import com.github.zigcat.ormlite.exception.NotFoundException;
import com.github.zigcat.ormlite.exception.RedirectionException;
import com.github.zigcat.ormlite.models.Category;
import com.github.zigcat.ormlite.models.CategoryGenre;
import com.github.zigcat.ormlite.models.Genre;

import java.io.IOException;
import java.sql.SQLException;

public class CategoryGenreDeserializer extends StdDeserializer<CategoryGenre> {
    protected CategoryGenreDeserializer(Class<?> vc) {
        super(vc);
    }

    protected CategoryGenreDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected CategoryGenreDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public CategoryGenreDeserializer(){
        super(CategoryGenre.class);
    }

    @Override
    public CategoryGenre deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Category category;
        Genre genre;
        try {
            int id = node.get("id").asInt();
            if(node.get("category") instanceof NullNode || node.get("category") == null){
                throw new NotFoundException("Category isn't valid(400)");
            } else {
                category = CategoryController.categoryService.getById(node.get("category").asInt());
            }
            if(node.get("genre") instanceof NullNode || node.get("genre") == null){
                throw new NotFoundException("Genre isn't valid(400)");
            } else {
                genre = GenreController.genreService.getById(node.get("genre").asInt());
            }
            return new CategoryGenre(id, genre, category);
        } catch (SQLException e){
            throw new RedirectionException("SQLException e");
        } catch (NullPointerException e){
            throw new CustomException("one of NotNull params is Null");
        }
    }
}
