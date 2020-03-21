package com.github.zigcat.ormlite.models;

import com.github.zigcat.ormlite.controllers.GenreController;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = "genre")
public class Genre {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static Genre getById(int id) throws SQLException {
        for(Genre g: GenreController.genreDao.queryForAll()){
            if(g.getId() == id){
                return g;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
