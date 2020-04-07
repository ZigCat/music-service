package com.github.zigcat;

import com.github.zigcat.ormlite.models.*;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/music_service?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_LOGIN = "root";
    private static final String DB_PASSWORD = "";
    public static JdbcPooledConnectionSource source;

    static{
        try {
            source = new JdbcPooledConnectionSource(DB_URL, DB_LOGIN, DB_PASSWORD);
            TableUtils.createTableIfNotExists(source, User.class);
            TableUtils.createTableIfNotExists(source, Music.class);
            TableUtils.createTableIfNotExists(source, UserMusic.class);
            TableUtils.createTableIfNotExists(source, Album.class);
            TableUtils.createTableIfNotExists(source, Author.class);
            TableUtils.createTableIfNotExists(source, Group.class);
            TableUtils.createTableIfNotExists(source, Genre.class);
            TableUtils.createTableIfNotExists(source, AuthorGroup.class);
            TableUtils.createTableIfNotExists(source, Category.class);
            TableUtils.createTableIfNotExists(source, CategoryGenre.class);
            TableUtils.createTableIfNotExists(source, Tag.class);
            TableUtils.createTableIfNotExists(source, TagAlbum.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
