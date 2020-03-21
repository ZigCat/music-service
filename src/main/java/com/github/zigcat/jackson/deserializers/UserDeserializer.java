package com.github.zigcat.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.zigcat.ormlite.models.Role;
import com.github.zigcat.ormlite.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

public class UserDeserializer extends StdDeserializer<User> {
    protected UserDeserializer(Class<?> vc) {
        super(vc);
    }

    protected UserDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected UserDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    public UserDeserializer(){
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode json = jsonParser.getCodec().readTree(jsonParser);
        int id = json.get("id").asInt();
        String name = json.get("name").asText();
        String surname = json.get("surname").asText();
        String email = json.get("email").asText();
        String password = BCrypt.hashpw(json.get("password").asText(), BCrypt.gensalt(12));
        String regDate = json.get("regDate").asText();
        String role = json.get("role").asText();
        if(regDate.equals("0000")){
            return new User(id, name, surname, email, password, Role.valueOf(role));
        } else {
            return new User(id, name, surname, email, password, LocalDate.parse(regDate, User.dateTimeFormatter),
                    Role.valueOf(role));
        }
    }
}
