package com.github.zigcat.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.zigcat.ormlite.controllers.AlbumController;
import com.github.zigcat.ormlite.controllers.AuthorController;
import com.github.zigcat.ormlite.controllers.GenreController;
import com.github.zigcat.ormlite.controllers.GroupController;
import com.github.zigcat.ormlite.models.*;

import java.io.IOException;
import java.sql.SQLException;

public class MusicSerializer extends StdSerializer<Music> {
    protected MusicSerializer(Class<Music> t) {
        super(t);
    }

    protected MusicSerializer(JavaType type) {
        super(type);
    }

    protected MusicSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected MusicSerializer(StdSerializer<?> src) {
        super(src);
    }

    public MusicSerializer(){
        super(Music.class);
    }

    @Override
    public void serialize(Music music, JsonGenerator json, SerializerProvider serializerProvider) throws IOException {
        try {
            json.writeStartObject();
            json.writeNumberField("id", music.getId());
            json.writeStringField("name", music.getName());
            json.writeStringField("creationDate", music.getCreationDate());
            json.writeObjectField("genre", GenreController.genreService.getById(music.getGenre().getId()));
            json.writeObjectField("author", music.getAuthor());
            json.writeObjectField("group", music.getGroup());
            json.writeObjectField("album", music.getAlbum());
            json.writeStringField("content", music.getContent());
            json.writeEndObject();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
