package com.github.zigcat.ormlite.models;

import com.github.zigcat.ormlite.controllers.AlbumController;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.time.LocalDate;

@DatabaseTable(tableName = "album")
public class Album {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String creationDate;

    @DatabaseField(foreign = true)
    private Author author;

    @DatabaseField(foreign = true)
    private Group group;

    private LocalDate creation;

    public boolean checkAlbum(Album otherAlbum){
        if(otherAlbum.getId() == id && otherAlbum.getName().equals(name)
                && otherAlbum.getCreationDate().equals(creationDate)){
            return true;
        }
        return false;
    }

    public static Album getById(int id) throws SQLException {
        for(Album a: AlbumController.albumDao.queryForAll()){
            if(a.getId() == id){
                return a;
            }
        }
        return null;
    }

    public Album(int id, String name, LocalDate creation, Author author, Group group) {
        this.id = id;
        this.name = name;
        this.creationDate = creation.format(User.dateTimeFormatter);
        this.author = author;
        this.group = group;
    }

    public Album() {
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }
}
